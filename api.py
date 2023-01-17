from flask import *
from database import *


api=Blueprint('api',__name__)


@api.route("/login")
def login():
	data={}

	uname=request.args['username']
	pwd=request.args['password']


	# print(uname,pwd)
	q="select * from login inner join customer using (login_id) where username='%s' and password='%s'"%(uname,pwd)
	res=select(q)
	if res:
		
		data['status']='success'
		data['data']=res
	else:
		data['status']='failed'
	return str(data)




@api.route("/viewmyac")
def viewmyac():
	data={}


	lid=request.args['lid']
	q="SELECT * FROM `account`,`bank` WHERE `account`.`bank_id`=`bank`.`bank_id` and customer_id=(select customer_id from customer where login_id='%s')"%(lid)
	# print(q)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']='failed'
	data['method']='viewmyac'
	return str(data)


@api.route("/moneytransfer")
def moneytransfer():
	data={}


	toaccount=request.args['toaccount']
	myaccount=request.args['myaccount']
	amount=request.args['amount']
	q="SELECT * FROM `account` where account_number like '%s' "%(toaccount)
	res=select(q)
	if res:
		q="insert into transaction values(null,'%s','%s','%s',curdate())"%(myaccount,toaccount,amount)
		insert(q)
		q="update account set balance=balance-'%s' where account_number='%s'"%(amount,myaccount)
		update(q)
		q="update account set balance=balance+'%s' where account_number='%s'"%(amount,toaccount)
		update(q)
		data['status']='success'
		data['data']=res
	else:
		data['status']='noaccount'
	# data['method']='moneytransfer'
	return str(data)



@api.route("/accountbalanceadd")
def accountbalanceadd():
	data={}


	account=request.args['account']
	amount=request.args['amount']
	q="update account set balance=balance+'%s' where account_number='%s'"%(amount,account)
	update(q)
	data['status']='success'

	
	# data['method']='accountbalanceadd'
	return str(data)


@api.route("/searchnumbers")
def searchnumbers():
	data={}


	num="%"+request.args['num']+"%"
	lid=request.args['lid']
	
	q="select * from customer inner join account using (customer_id) where phone like '%s' and customer_id <> (select customer_id from customer where login_id='%s')"%(num,lid)
	# print(num)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
		data['data']=res
	data['method']='searchnumbers'
	return str(data)


@api.route("/moneytransferwithnnum")
def moneytransferwithnnum():
	data={}


	toaccount=request.args['toaccount']
	myaccount=request.args['myaccount']
	amount=request.args['amount']
	q="SELECT * FROM `account` where account_number like '%s' "%(toaccount)
	res=select(q)
	if res:
		q="insert into transaction values(null,'%s','%s','%s',curdate())"%(myaccount,toaccount,amount)
		insert(q)
		q="update account set balance=balance-'%s' where account_number='%s'"%(amount,myaccount)
		update(q)
		q="update account set balance=balance+'%s' where account_number='%s'"%(amount,toaccount)
		update(q)
		data['status']='success'
		data['data']=res
	else:
		data['status']='noaccount'
	# data['method']='moneytransfer'
	return str(data)



@api.route("/moneytransferwithQR")
def moneytransferwithQR():
	data={}


	acid=request.args['acid']
	myaccount=request.args['myaccount']
	amount=request.args['amount']
	
	q="SELECT * FROM `account` where account_number like (select account_number from account  where account_id='%s') "%(acid)
	res=select(q)
	if res:
		q="insert into transaction values(null,'%s',(select account_number from account  where account_id='%s'),'%s',curdate())"%(myaccount,acid,amount)
		insert(q)
		q="update account set balance=balance-'%s' where account_number='%s'"%(amount,myaccount)
		update(q)
		# print(q)
		q="update account set balance=balance+'%s' where account_id='%s'"%(amount,acid)
		# print(q)
		update(q)
		data['status']='success'
		data['data']=res
	else:
		data['status']='noaccount'
	# data['method']='moneytransfer'
	return str(data) 




@api.route("/viewmyhistory")
def viewmyhistory():
	data={}
	myac=request.args['myac']
	
	q="select * from transaction where from_ac='%s' or to_ac='%s'"%(myac,myac)
	# print(q)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
		
	data['method']='viewmyhistory'
	return str(data)


@api.route("/viewavailableLoans")
def viewavailableLoans():
	data={}
	lid=request.args['lid']
	
	q="select * from loan "
	# print(q)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
	
	data['method']='viewavailableLoans'
	return str(data)

@api.route("/requestloan")
def requestloan():
	data={}
	lid=request.args['lid']
	loanid=request.args['loan_id']

	q="select * from loan where loan_id in (select loan_id from loanrequest where loan_id='%s' and customer_id =(select customer_id from customer where login_id='%s'))"%(loanid,lid)
	val=select(q)
	if val:
		data['status']='already'
	else:
		q="insert into loanrequest values (null,(select customer_id from customer where login_id='%s'),'%s',now(),'pending')"%(lid,loanid)
		insert(q)
		data['status']='success'
	
	data['method']='requestloan'
	return str(data)


@api.route("/myloandetails")
def myloandetails():
	data={}
	lid=request.args['lid']
	
	q="select * from loan inner join loanrequest using (loan_id) where loanrequest.customer_id=(select customer_id from customer where login_id='%s')  "%(lid)
	# print(q)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
	
	data['method']='myloandetails'
	return str(data)


@api.route("/viewemployee")
def viewemployee():
	data={}

	
	q="select *,employee.login_id as login_id  from employee inner join bank using (bank_id)"

	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
	
	data['method']='viewemployee'
	return str(data)
	
	 

@api.route("/chatdetail")
def chatdetail():
	sid=request.args['sender_id']
	rid=request.args['receiver_id']
	
	data={}
	q="select * from chat where (sender_id='%s' and receiver_id='%s') or (sender_id='%s' and receiver_id='%s') group by chat_id "%(sid,rid,rid,sid)
	
	res=select(q)
	if res:
		data['status']="success"
		data['data']=res
	else:
		
		data['status']="failed"
	data['method']='chatdetail'
	
	return str(data)

@api.route("/chat")
def chat():
    data={}
    sid=request.args['sender_id']
    rid=request.args['receiver_id']
    det=request.args['details']

    q="insert into chat values(null,'%s','%s','%s',curdate())"%(sid,rid,det) 
    insert(q)
    data['status']='success'
    data['method']='chat'
    return str(data)




@api.route("/addcomplaint")
def addcomplaint():
    complaint=request.args['complaint']
    lid=request.args['lid']
   
    data={}

    q="insert into complaint values (null,(select customer_id from customer where login_id='%s'),'%s','pending',curdate())"%(lid,complaint)
    insert(q)
    data['status']="success"
    data['method']="addcomplaint"
    return str(data)
  
    
@api.route('/viewcomplaint')
def viewcomplaint():
    data={}
    lid=request.args['lid']
    z="select * from complaint where customer_id= (select customer_id from customer where login_id='%s')"%(lid)
    res=select(z)

    if res:
        data['status']='success'
        data['data']=res
    else:
        data['status']='failed'
    data['method']="viewcomplaint"
    return str(data)
