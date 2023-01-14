from flask import *
from database import *
employee=Blueprint('employee',__name__)

@employee.route('/employee_home')
def employee_home():
	return render_template('employee_home.html')


@employee.route('/employee_view_profile')
def employee_view_profile():
	data={}
	q="select * from employee where employee_id='%s'"%(session['employee_id'])
	res=select(q)
	data['profile']=res
	return render_template("employee_view_profile.html",data=data)


@employee.route('/employee_view_loanrequest')
def employee_view_loanrequest():
	data={}
	q="SELECT * FROM loan INNER JOIN loanrequest USING(loan_id) INNER JOIN customer USING(customer_id)INNER JOIN employee ON employee.employee_id where employee_id='%s'"%(session['employee_id'])
	res=select(q)
	data['request']=res

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']

	else:
		action=None

	if action=="accept":
		q="update loanrequest set status='accepted' where request_id='%s'"%(id)
		update(q)
		flash('accepted successfully')
		return redirect(url_for('employee.employee_view_loanrequest'))

	if action=="reject":
		q="update loanrequest set status='rejected' where request_id='%s'"%(id)
		update(q)
		flash('rejected successfully')
		return redirect(url_for('employee.employee_view_loanrequest'))


	return render_template("employee_view_loanrequest.html",data=data)


@employee.route('/employee_manage_ac_nd_customer',methods=['get','post'])
def employee_manage_ac_nd_customer():
	data={}

	q="SELECT * ,CONCAT(`customer`.house_name) AS house, CONCAT(customer.place) AS cust_place,CONCAT(customer.pincode) AS pin,CONCAT(customer.email) AS cust_email,CONCAT(customer.phone) as cust_phone FROM customer INNER JOIN `account` USING(customer_id)inner join bank using(bank_id) INNER JOIN employee ON employee.employee_id   WHERE  bank.bank_id=(SELECT bank_id FROM employee WHERE employee_id='%s') GROUP BY customer_id"%(session['employee_id'])
	res=select(q)
	data['ac']=res

	q="select * from bank"
	res=select(q)
	data['banks']=res


	if 'submit' in request.form:
		fname=request.form['first_name']
		lname=request.form['last_name']
		house=request.form['house_name']
		place=request.form['place']
		pin=request.form['pincode']
		phone=request.form['phone']
		email=request.form['email']
		bank=request.form['bank']
		
		balance=request.form['balance']
		uname=request.form['uname']
		pas=request.form['pass']
		key=request.form['key']

		import random

		ac =  random.randrange(10000000000, 99999999999)

		q="insert into login values(null,'%s','%s','customer')"%(uname,pas)
		id=insert(q)
		q="insert into customer values(null,'%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(id,key,fname,lname,house,place,pin,phone,email)
		insert(q)
		cust_id=insert(q)

		q="insert into account values(null,'%s','%s','%s','%s',now(),'pending')"%(cust_id,bank,ac,balance)
		insert(q)
		flash('inserted successfully')
		return redirect(url_for('employee.employee_manage_ac_nd_customer'))


	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']

	else:
		action=None

	if action=="delete":
		q="delete from customer where customer_id='%s'"%(id)
		delete(q)
		flash('deleted successfully')
		return redirect(url_for('employee.employee_manage_ac_nd_customer'))

	if action=="update":
		q="select * from customer where customer_id='%s'"%(id)
		res=select(q)
		data['up']=res

	if 'update' in request.form:
		fname=request.form['first_name']
		lname=request.form['last_name']
		house=request.form['house_name']
		place=request.form['place']
		pin=request.form['pincode']
		phone=request.form['phone']
		email=request.form['email']
		q="update customer set first_name='%s',last_name='%s',house_name='%s',place='%s',pincode='%s',phone='%s',email='%s' where customer_id='%s'"%(fname,lname,house,place,pin,phone,email,id)
		update(q)
		flash('updated successfully')
		return redirect(url_for('employee.employee_manage_ac_nd_customer'))


	return render_template("employee_manage_ac_nd_customer.html",data=data)



@employee.route('/employee_deposit_amt',methods=['get','post'])
def employee_deposit_amt():
	if 'submit' in request.form:
		frm=request.form['from']
		to=request.form['to']
		amt=request.form['amt']
		q="SELECT * FROM ACCOUNT WHERE  account_number='%s'"%(frm)
		print(q)
		res=select(q)
		frm_ac_amt=res[0]['balance']
		print('================',frm_ac_amt)
		if res:
			q="SELECT * FROM ACCOUNT WHERE account_number='%s'"%(to)
			print(q)
			res1=select(q)
			
			if res1:
				print('==================',to_ac_amt)
				to_ac_amt=res1[0]['balance']
				if frm_ac_amt < to_ac_amt:
					flash("insufficient Amount")
				else:
					q="insert into transaction values(null,'%s','%s','%s',now())"%(frm,to,amt)
					insert(q)
					q="update account set balance='%s'-'%s' where account_number='%s'"%(frm_ac_amt,amt,frm)
					update(q)
					q="update account set balance='%s'+'%s' where account_number='%s'"%(to_ac_amt,amt,to)
					update(q)
					flash('inserted successfully')
					return redirect(url_for('employee.employee_deposit_amt'))

				# else:
				# 	flash('check your balance!')
				# 	return redirect(url_for('employee.employee_deposit_amt'))

			else:
				flash('plaese enter valid To account number')
				return redirect(url_for('employee.employee_deposit_amt'))
				
		else:
			flash('plaese enter valid from account number')
			return redirect(url_for('employee.employee_deposit_amt'))
			
		

	return render_template("employee_deposit_amt.html")


@employee.route('/employee_msg_bank')
def employee_msg_bank():
	data={}
	q="SELECT *,CONCAT(bank.email) AS b_email,CONCAT(bank.phone) AS b_phone FROM bank INNER JOIN employee USING(bank_id) WHERE employee_id='%s'"%(session['employee_id'])
	res=select(q)
	data['bank']=res

	return render_template("employee_msg_bank.html",data=data)



@employee.route('/employee_chat_bank',methods=['get','post'])
def employee_chat_bank():
	data={}

	id=request.args['id']
	uid=session['lid']
	data['employee']=uid

	q="select * from chat where (sender_id='%s' and receiver_id='%s') or (receiver_id='%s' or sender_id='%s')"%(uid,id,uid,id)
	res1=select(q)
	data['msg']=res1
	if 'submit' in request.form:
		msg=request.form['message']
		q="insert into chat values(null,'%s','%s','%s',curdate())"%(uid,id,msg)
		insert(q)
		return redirect(url_for('employee.employee_chat_bank',id=id))

	return render_template("employee_chat_bank.html",data=data)



@employee.route('/employee_msg_customer')
def employee_msg_customer():
	data={}
	q="SELECT *,CONCAT(`customer`.place) AS cust_place,CONCAT(`customer`.phone) as cust_phone , concat (`customer`.email) as cust_email FROM customer INNER JOIN ACCOUNT USING(customer_id) INNER JOIN bank USING(bank_id)  WHERE  bank.bank_id=(SELECT bank_id FROM employee WHERE employee_id='%s') GROUP BY customer_id"%(session['employee_id'])
	print(q)
	res=select(q)
	data['customer']=res

	return render_template("employee_msg_customer.html",data=data)



@employee.route('/employee_chat_customer',methods=['get','post'])
def employee_chat_customer():
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
		return redirect(url_for('employee.employee_chat_customer',receiver_id=receiver_id))

	return render_template("employee_chat_customer.html",data=data)