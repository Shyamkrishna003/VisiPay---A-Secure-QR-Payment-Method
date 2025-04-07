from flask import *
from database import *
admin=Blueprint('admin',__name__)

@admin.route('/admin_home')
def admin_home():
	return render_template('admin_home.html')


@admin.route('/admin_manage_bank',methods=['get','post'])
def admin_manage_bank():
	data={}
	q="select * from bank"
	res=select(q)
	data['bank']=res

	if 'submit' in request.form:
		banks=request.form['bank']
		place=request.form['place']
		phone=request.form['phone']
		email=request.form['email']
		uname=request.form['uname']
		pas=request.form['pass']
		q="insert into login values(null,'%s','%s','bank')"%(uname,pas)
		id=insert(q)
		q="insert into bank values(null,'%s','%s','%s','%s','%s')"%(id,banks,place,phone,email)
		insert(q)
		print(q)
		flash('inserted successfully')
		return redirect(url_for('admin.admin_manage_bank'))

	if 'action' in request.args:
		action=request.args['action']
		id=request.args['id']

	else:
		action=None

	if action=="delete":
		q="delete from bank where bank_id='%s'"%(id)
		update(q)
		return redirect(url_for('admin.admin_manage_bank'))

	if action=="update":
		q="select * from bank where bank_id='%s'"%(id)
		res=select(q)
		data['up']=res

	if 'update' in request.form:
		banks=request.form['bank']
		place=request.form['place']
		phone=request.form['phone']
		email=request.form['email']
		q="update bank set bank_name='%s',place='%s',phone='%s',email='%s' where bank_id='%s'"%(banks,place,phone,email,id)
		update(q)
		flash('updated successfully')
		return redirect(url_for('admin.admin_manage_bank'))

	return render_template("admin_manage_bank.html",data=data)





@admin.route('/admin_view_customer_nd_ac')
def admin_view_customer_nd_ac():
	data={}
	q="select * from account inner join bank using(bank_id) inner join customer using (customer_id)"
	res=select(q)
	data['ac']=res
	return render_template("admin_view_customers_nd_ac.html",data=data)



@admin.route('/admin_view_loandetail')
def admin_view_loandetail():
	data={}
	q="select * from loan inner join bank using(bank_id)"
	res=select(q)
	data['loan']=res
	return render_template("admin_view_loandetail.html",data=data)



@admin.route('/admin_view_loanrequest')
def admin_view_loanrequest():
	data={}
	q="select * from loan inner join loanrequest using(loan_id) inner join customer using(customer_id)"
	res=select(q)
	data['request']=res
	return render_template("admin_view_loanrequest.html",data=data)




@admin.route('admin_view_complaint',methods=['get','post'])
def admin_view_complaint():
	data={}
	q="SELECT concat( f_name ,' ' ,l_name) as name,complaint,replay ,date,complaint_id  FROM complaint inner join customer on complaint.customer_id=customer.login_id union SELECT merchantname as name,complaint,replay ,date,complaint_id  FROM complaint INNER JOIN merchant ON complaint.customer_id=merchant.login_id"
	res=select(q)
	data['complaint']=res

	return render_template("admin_view_complaint.html",data=data)



@admin.route('/admin_send_replay',methods=['get','post'])
def admin_send_replay():
	id=request.args['id']
	data={}
	q="select * from complaint where complaint_id='%s'"%(id)
	res=select(q)
	data['send']=res		
		
	if 'submit' in request.form:
		re=request.form['replay']
		q="UPDATE complaint SET replay='%s' WHERE complaint_id='%s'"%(re,id)
		update(q)
		flash("replied successfully")
		return redirect(url_for('admin.admin_view_complaint',id=id))

	return render_template("admin_send_replay.html",data=data)






# <-------------------Secure Money Transfer ------------------->



@admin.route('/admin_view_merchant',methods=['GET','POST'])
def admin_view_merchant():
    data={}
    s="select * from merchant inner join login using(login_id)"
    #  where usertype='pending' or usertype='rejected'
    data['merchant']=select(s)
    if 'action' in request.args:
        login_id=request.args['login_id']
        action=request.args['action']
    else: action=None
    if action=='accept':
        s="update login set usertype='merchant' where login_id='%s'"%(login_id)
        delete(s)
        s="insert into wallet values(null,'%s',0,'merchant')"%(login_id)
        insert(s)
        return redirect(url_for('admin.admin_view_merchant'))
    if action=='reject':
        s="update login set usertype='rejected' where login_id='%s'"%(login_id)
        delete(s)
        return redirect(url_for('admin.admin_view_merchant'))
    return render_template('admin_view_merchant.html',data=data)



@admin.route('/admin_view_user',methods=['GET','POST'])
def admin_view_user():
    data={}
    s="select * from customer"
    data['user']=select(s)
    return render_template('admin_view_user.html',data=data)



@admin.route('/admin_send_notification', methods=['GET', 'POST'])
def admin_send_notification():
    data = {}  
    s="select * from notification"
    data['n']=select(s)
    if 'submit' in request.form:
        notification = request.form['notification']
        s = "insert into notification values(null, 0, '%s', curdate())" % (notification)
        data['notification'] = insert(s)
        flash("Your Notification Sented Successfully")
        return redirect(url_for('admin.admin_send_notification'))
    return render_template('admin_send_notification.html',data=data)


