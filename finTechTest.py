import requests, json

#Register

request_dict = {"mobileNumber":"8899889988","username" : "sfsdf", "password":"xx","confirmPassword":"xx"}

response = requests.post("https://serene-peak-1114.herokuapp.com/register", data=json.dumps(request_dict))

print response.content


#Login


request_dict = {"mobileNumber":"8899889988", "password":"xx"}

response = requests.post("https://serene-peak-1114.herokuapp.com/login", data=json.dumps(request_dict))

print response.content


#Get list of malls

response = requests.get("https://serene-peak-1114.herokuapp.com/getMallsList")

print response.content

#get items available in mall
request_dict = {"mallId":1}

response = requests.post("https://serene-peak-1114.herokuapp.com/getAvailableItems", data=json.dumps(request_dict))

print response.content


#checkout
request_dict = { "mallId": 1,"userId":2, "cart" : [ { "itemId":1, "quantity":2}, {"itemId":4, "quantity":3 } ,{"itemId":7, "quantity":1 } ] }

response = requests.post("https://serene-peak-1114.herokuapp.com/checkout", data=json.dumps(request_dict))

print response.content


#payment
request_dict = {"transactionId":"zebqxo" , "paymentMethod": "OnlinePayment" }

response = requests.post("https://serene-peak-1114.herokuapp.com/payment", data=json.dumps(request_dict))

print response.content


