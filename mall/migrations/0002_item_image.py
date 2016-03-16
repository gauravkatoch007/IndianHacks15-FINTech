# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('mall', '0001_initial'),
    ]

    operations = [
        migrations.AddField(
            model_name='item',
            name='image',
            field=models.TextField(default='http://ak1.polyvoreimg.com/cgi/img-set/cid/129492397/id/5Lr9QocS5BGUiBmLmu7EJQ/size/y.jpg', max_length=1000),
            preserve_default=False,
        ),
    ]
