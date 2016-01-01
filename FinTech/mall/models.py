from django.db import models

# Create your models here.

class Mall(models.Model):
    mallName = models.CharField(max_length=200, unique=True)
    def __unicode__(self):
        return self.mallName

class Item(models.Model):
    itemName = models.CharField(max_length=200)
    mall = models.ForeignKey(Mall)
    description = models.CharField(max_length=200)
    price = models.IntegerField(default=0)
    unit = models.CharField(max_length=20)
    barcode = models.CharField(max_length=200)
    def __unicode__(self):
        return self.itemName

