from flask import *
from database import *
bank=Blueprint('bank',__name__)

@bank.route('/bank_home')
def bank_home():
	return render_template('bank_home.html')


@bank.route('/bank_manage_employee',methods=['get','post'])
def bank_manage_employee():
	data={}
	
	q="select * from employee inner join login using(login_id) where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['employee']=res

	if 'submit' in request.form:
		fname=request.form['fname']
		lname=request.form['lname']
		house=request.form['house']
		place=request.form['place']
		gender=request.form['gender']
		phone=request.form['phone']
		email=request.form['email']
		qualification=request.form['qualification']
		uname=request.form['uname']
		pas=request.form['pass']
		q="insert into login values(null,'%s','%s','employee')"%(uname,pas)
		id=insert(q)
		q="insert into employee values(null,'%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(id,session['bank_id'],fname,lname,house,place,gender,phone,email,qualification)
		insert(q)
		print(q)
		flash('inserted successfully')
		return redirect(url_for('bank.bank_manage_employee'))

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']
		lid=request.args['lid']

	else:
		action=None

	if action=="delete":
		q="delete from employee where employee_id='%s'"%(id)
		delete(q)
		
		return redirect(url_for('bank.bank_manage_employee'))

	if action=="update":
		q="select * from employee where employee_id='%s'"%(id)
		res=select(q)
		data['up']=res

	if 'update' in request.form:
		fname=request.form['fname']
		lname=request.form['lname']
		house=request.form['house']
		place=request.form['place']
		gender=request.form['gender']
		phone=request.form['phone']
		email=request.form['email']
		qualification=request.form['qualification']
		
		q="update employee set first_name='%s',last_name='%s',house_name='%s',place='%s',gender='%s',phone='%s',email='%s' where employee_id='%s'"%(fname,lname,house,place,gender,phone,email,id)
		update(q)
		flash('updated successfully')

		return redirect(url_for('bank.bank_manage_employee'))

	return render_template("bank_manage_employee.html",data=data)



@bank.route('/bank_view_customer_nd_ac')
def bank_view_customer_nd_ac():
	data={}
	q="select * from account inner join bank using(bank_id) inner join customer using (customer_id) where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['ac']=res
	return render_template("bank_view_customer_nd_ac.html",data=data)


@bank.route('/bank_manage_loandetail',methods=['get','post'])
def bank_manage_loandetail():
	data={}
	bid=session['bank_id']
	q="select * from loan where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['loan']=res

	if 'submit' in request.form:
		loan=request.form['loan']
		detail=request.form['detail']
		interest=request.form['interest']
		amount=request.form['amount']
		q="insert into loan values(null,'%s','%s','%s','%s','%s')"%(bid,loan,detail,interest,amount)
		insert(q)
		print(q)
		flash('inserted successfully')
		return redirect(url_for('bank.bank_manage_loandetail'))

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']

	else:
		action=None

	if action=="delete":
		q="delete from loan where loan_id='%s'"%(id)
		delete(q)
		flash('deleted successfully ')
		return redirect(url_for('bank.bank_manage_loandetail'))

	if action=="update":
		q="select * from loan where loan_id='%s'"%(id)
		res=select(q)
		data['up']=res

	if 'update' in request.form:
		loan=request.form['loan']
		detail=request.form['detail']
		interest=request.form['interest']
		amount=request.form['amount']
		q="update loan set loan_name='%s',details='%s',interest_name='%s',max_amount='%s' where loan_id='%s'"%(loan,detail,interest,amount,id)
		update(q)
		flash('updated successfully')
		return redirect(url_for('bank.bank_manage_loandetail'))

	return render_template("bank_manage_loandetail.html",data=data)



@bank.route('/bank_manage_notification',methods=['get','post'])
def bank_manage_notification():
	data={}
	bid=session['bank_id']
	q="select * from notification where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['notification']=res

	if 'submit' in request.form:
		
		notification=request.form['notification']
		q="insert into notification values(null,'%s','%s',curdate())"%(bid,notification)
		insert(q)
		print(q)
		flash('inserted successfully')
		return redirect(url_for('bank.bank_manage_notification'))

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']

	else:
		action=None

	if action=="delete":
		q="delete from notification where notification_id='%s'"%(id)
		delete(q)
		flash('deleted successfully')
		return redirect(url_for('bank.bank_manage_notification'))

	if action=="update":
		q="select * from notification where notification_id='%s'"%(id)
		res=select(q)
		data['up']=res

	if 'update' in request.form:
		
		notification=request.form['notification']
		q="update notification set notification='%s' where notification_id='%s'"%(notification,id)
		update(q)
		flash('updated successfully')
		return redirect(url_for('bank.bank_manage_notification'))

	return render_template("bank_manage_notification.html",data=data)


@bank.route('/bank_view_loanrequest')
def bank_view_loanrequest():
	data={}
	q="select * from loan inner join loanrequest using(loan_id) inner join customer using(customer_id) where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['request']=res
	return render_template("bank_view_loanrequest.html",data=data)


@bank.route('/bank_msg_emp')
def bank_msg_emp():
	data={}
	q="select * from employee where bank_id='%s'"%(session['bank_id'])
	res=select(q)
	data['employee']=res

	return render_template("bank_msg_emp.html",data=data)

@bank.route('/bank_chat_employee',methods=['get','post'])
def bank_chat_employee():
	data={}
	receiver_id=request.args['receiver_id']
	sender_id=session['lid']
	data['bank']=sender_id

	q="select * from chat where (sender_id='%s' and receiver_id='%s') or (receiver_id='%s' and sender_id='%s')"%(sender_id,receiver_id,sender_id,receiver_id)
	print(q)
	res1=select(q)
	data['msg']=res1

	
	if 'submit' in request.form:
		msg=request.form['message']
		q="insert into chat values(null,'%s','%s','%s',curdate())"%(sender_id,receiver_id,msg)
		insert(q)
		flash('send message')
		return redirect(url_for('bank.bank_chat_employee',receiver_id=receiver_id))

	return render_template("bank_chat_employee.html",data=data)

