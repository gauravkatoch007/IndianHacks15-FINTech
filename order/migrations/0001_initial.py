# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        ('mall', '0001_initial'),
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Cart',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('quantity', models.IntegerField(default=1)),
                ('item', models.ForeignKey(to='mall.Item')),
            ],
        ),
        migrations.CreateModel(
            name='Order',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('counter', models.CharField(max_length=20)),
                ('created', models.DateTimeField(auto_now_add=True)),
                ('status', models.IntegerField(choices=[(0, b'RECEIVED'), (1, b'OLP_PENDING'), (2, b'Processed'), (3, b'Dispatched'), (4, b'Handed Over'), (5, b'CANCELED')])),
                ('paymentMethod', models.IntegerField(choices=[(0, b'Cash On Delivery'), (1, b'Online Payment')])),
                ('transactionId', models.CharField(max_length=20)),
                ('mall', models.ForeignKey(to='mall.Mall')),
                ('user', models.ForeignKey(to=settings.AUTH_USER_MODEL)),
            ],
        ),
        migrations.AddField(
            model_name='cart',
            name='order',
            field=models.ForeignKey(to='order.Order'),
        ),
    ]
