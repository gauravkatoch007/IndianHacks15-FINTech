# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('order', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='order',
            name='counter',
            field=models.CharField(default=1, max_length=20),
        ),
        migrations.AlterField(
            model_name='order',
            name='paymentMethod',
            field=models.IntegerField(default=0, choices=[(0, b'Cash On Delivery'), (1, b'Online Payment')]),
        ),
        migrations.AlterField(
            model_name='order',
            name='status',
            field=models.IntegerField(default=0, choices=[(0, b'RECEIVED'), (1, b'OLP_PENDING'), (2, b'Processed'), (3, b'Dispatched'), (4, b'Handed Over'), (5, b'CANCELED')]),
        ),
        migrations.AlterField(
            model_name='order',
            name='transactionId',
            field=models.CharField(unique=True, max_length=20),
        ),
    ]
