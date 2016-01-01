# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='Item',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('itemName', models.CharField(max_length=200)),
                ('description', models.CharField(max_length=200)),
                ('price', models.IntegerField(default=0)),
                ('unit', models.CharField(max_length=20)),
                ('barcode', models.CharField(max_length=200)),
            ],
        ),
        migrations.CreateModel(
            name='Mall',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('mallName', models.CharField(unique=True, max_length=200)),
            ],
        ),
        migrations.AddField(
            model_name='item',
            name='mall',
            field=models.ForeignKey(to='mall.Mall'),
        ),
    ]
