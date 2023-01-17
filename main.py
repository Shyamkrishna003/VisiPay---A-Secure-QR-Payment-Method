from flask import Flask
from public import public
from admin import admin
from bank import bank
from employee import employee
from api import api






app=Flask(__name__)
app.secret_key="btech"
app.register_blueprint(public)
app.register_blueprint(admin,url_prefix='/admin')
app.register_blueprint(bank,url_prefix='/bank')
app.register_blueprint(employee,url_prefix='/employee')
app.register_blueprint(api,url_prefix='/api')



app.run(debug=True,port=5010,host="0.0.0.0")




