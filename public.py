from flask import *
from database import *
public=Blueprint('public',__name__)

@public.route('/')
def home():
	return render_template('public_home.html')

@public.route('/login',methods=['get','post'])
def login():
	if 'submit' in request.form:
		username=request.form['username']
		password=request.form['password']
		
		q="select * from login where username='%s' and password='%s'"%(username,password)
		res=select(q)
		print(res)
		print(q)
		if res:
			session['lid']=res[0]['login_id']
			lid=session['lid']
			if res[0]['usertype']=='admin':
				return redirect(url_for('admin.admin_home'))
			else:
				flash("Invalid Username or Passoword")
				return redirect(url_for('public.login'))


			# elif res[0]['usertype']=='bank':
			# 	q="select * from bank where login_id='%s'"%(lid)
			# 	res=select(q)
			# 	print(q)
			# 	if res:
			# 		session['bank_id']=res[0]['bank_id']
			# 		print(session['bank_id'])		
			# 	return redirect(url_for('bank.bank_home'))


			# elif res[0]['usertype']=='merchant':
			# 	q="select * from merchant where login_id='%s'"%(lid)
			# 	res=select(q)
			# 	print(q)
			# 	if res:
			# 		session['merchant_id']=res[0]['merchant_id']
			# 		print(session['merchant_id'])		
			# 	return redirect(url_for('merchant.merchant_home'))


			# elif res[0]['usertype']=='employee':
			# 	q="select * from employee where login_id='%s'"%(lid)
			# 	res=select(q)
			# 	if res:
			# 		session['employee_id']=res[0]['employee_id']
			# 	return redirect(url_for('employee.employee_home'))

			# elif res[0]['usertype']=='customer':
			# 	q="select * from customer where login_id='%s'"%(lid)
			# 	res=select(q)
			# 	if res:
			# 		session['customer_id']=res[0]['customer_id']
			# 	return redirect(url_for('customer.customer'))
		else :
			flash("Invalid Username or Passoword")
				


		return redirect(url_for('public.login'))

	return render_template('login.html')




# <----------------------- Secure Money --------------------->

@public.route('/merchant_register',methods=['GET','POST'])
def counselor_register():
    if 'merchant_register' in request.form:
        name=request.form['name']
        place=request.form['place']
        phone=request.form['phone']
        email=request.form['email']
        username=request.form['username']
        password=request.form['password']
        s="select * from login where username='%s'"%(username)
        check=select(s)
        if check:
            flash('Username already exist')
        else:
            a="insert into login values(null,'%s','%s','pending')"%(username,password)
            login_id=insert(a)
            b="insert into merchant values(null,'%s','%s','%s','%s','%s')"%(login_id,name,place,phone,email)
            insert(b)
            flash('Registerd Sucessfully')
            return redirect(url_for('public.login'))
    return render_template('merchant_register.html')
