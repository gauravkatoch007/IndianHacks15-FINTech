from django.shortcuts import HttpResponse
from django.shortcuts import render_to_response, HttpResponseRedirect
from django.contrib import auth
from django.shortcuts import render
import json
from django.contrib.auth.models import User
from django.core.exceptions import ObjectDoesNotExist
from mall.models import  Mall,Item
from order.models import Order,Cart
from django.db import IntegrityError
import string,random,time

def landingPage(request):
    return render_to_response('landingPage.html')

def register(request):
    if request.method == 'POST':
        dataDict = json.loads(request.body)
        username = dataDict['username']
        mobileNumber = dataDict['mobileNumber']
        password = dataDict['password']
        confirmPassword = dataDict['confirmPassword']

        if password != confirmPassword:
            status = {"status" :False,"message": "Password and Confirm Password don't match" }
            return HttpResponse(json.dumps(status), content_type = "application/json")
        else:

            try:
                user = User.objects.get(username=mobileNumber)
            except ObjectDoesNotExist:
                print "User not already registered"
                user = User.objects.create_user(mobileNumber,'dummy@email.com', password)

            user.first_name = username
            user.set_password(password)
            user.save()
            status = {"status" :True,"message": "User Created Successfully","username" : user.first_name,  "userId":user.id }
            return HttpResponse(json.dumps(status), content_type = "application/json")


    else:
        status = {"status" :False,"message": "Send Post request" }
        return HttpResponse(json.dumps(status), content_type = "application/json")

def login(request):
    if request.method == 'POST':
        dataDictionary = json.loads(request.body)
        # print dataDictionary

        username = dataDictionary['mobileNumber']
        password = dataDictionary['password']

        print username,password

        user = auth.authenticate(username=username, password=password)
        if user is not None:
            auth.login(request, user)
            status = {"status" :True,"message": "Logged in Successfully","userId":user.id,"username" : user.first_name }

            return HttpResponse(json.dumps(status), content_type = "application/json")
        else:
            print "authentication failure"
            status = {"status" :False,"message": "Invalid Mobile Number and Password Combination" }
            return HttpResponse(json.dumps(status), content_type = "application/json")
    else:
        status = {"status" :False,"message": "Send Post request" }
        return HttpResponse(json.dumps(status), content_type = "application/json")

def getMallsList(request):
    mallList = []

    malls = Mall.objects.all()

    for mall in malls:
        dic = { "mallName": mall.mallName, "id":mall.id}
        mallList.append(dic)

    args = {"mallList" :mallList}
    return HttpResponse(json.dumps(args), content_type = "application/json")

def getAvailableItems(request):
    if request.method == 'POST':
        dataDictionary = json.loads(request.body)
        # print dataDictionary

        mallId = dataDictionary['mallId']



        mall = Mall.objects.get(id = mallId)

        items = Item.objects.filter(mall_id = mall.id)
        itemList = []
        for item in items:
            dic = {"itemId":item.id,"itemName":item.itemName, "description":item.description, "price":item.price,"unit":item.unit,"barcode": item.barcode}
            itemList.append(dic)


        args = {"itemList" :itemList}
        return HttpResponse(json.dumps(args), content_type = "application/json")
    else:
        status = {"status" :False,"message": "Send Post request" }
        return HttpResponse(json.dumps(status), content_type = "application/json")

def checkout(request):
    if request.method == 'POST':
        dataDictionary = json.loads(request.body)
        # print dataDictionary

        mallId = dataDictionary['mallId']
        userId = dataDictionary['userId']
        cart = dataDictionary['cart']

        user = User.objects.get(id = userId)
        mall = Mall.objects.get(id = mallId)
        order = Order(user = user,mall = mall)

        generateUniqueTransactionId(order)



        mall = Mall.objects.get(id = mallId)

        items = Item.objects.filter(mall_id = mall.id)
        itemList = []
        for item in items:
            dic = {"itemId":item.id,"itemName":item.itemName, "description":item.description, "price":item.price,"unit":item.unit,"barcode": item.barcode}
            itemList.append(dic)

        amountToPay = 0
        for cartItem in cart:
            item = Item.objects.get(id = cartItem['itemId'])
            cartObject = Cart(item = item, quantity = cartItem['quantity'],order = order)
            cartObject.save()
            amountToPay += item.price *cartItem['quantity']

        args = { "success": True, "transactionId":order.transactionId, "amountToPay":amountToPay, "message":"Order created sussessfully"}
        return HttpResponse(json.dumps(args), content_type = "application/json")
    else:
        status = {"status" :False,"message": "Send Post request" }
        return HttpResponse(json.dumps(status), content_type = "application/json")


def generateUniqueTransactionId(order):
    characterSet = string.ascii_lowercase + string.digits
    print "characterSet ->", characterSet
    # to allow for repetetions
    sampleSet = characterSet*6
    successCount = 0
    i = 0

    while(i<500):

        transactionId = "".join(random.sample(sampleSet,6))
        print "transactionId->", transactionId
        order.transactionId = transactionId
        try:
            order.save()
            successCount+=1
            break
        except IntegrityError as Ierr:
            #print "A key got repeated"
            print Ierr
            i=i+1
            continue
        except Exception as e:
            print "An unexpected error occurred"
            print e
            time.sleep(5)
            i=i+1
            raise e

    print "6 digit order ID->", order.transactionId
    return order.transactionId


def payment(request):
    if request.method == 'POST':
        dataDictionary = json.loads(request.body)
        # print dataDictionary

        transactionId = dataDictionary['transactionId']
        paymentMethod = dataDictionary['paymentMethod']

        order = Order.objects.get(transactionId = transactionId)

        if paymentMethod.lower() == "onlinepayment":
            order.paymentMethod = Order.ONLINEPAYMENT
            order.save()

        if order.paymentMethod == Order.ONLINEPAYMENT:
            message = "Thanks for paying online."
        else:
            message = "Please pay by cash on counter while collecting bag"
        args = { "success": True,  "message": message, "counter" : order.counter}
        return HttpResponse(json.dumps(args), content_type = "application/json")
    else:
        status = {"status" :False,"message": "Send Post request" }
        return HttpResponse(json.dumps(status), content_type = "application/json")