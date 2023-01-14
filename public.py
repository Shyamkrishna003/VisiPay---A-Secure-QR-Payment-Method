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


			elif res[0]['usertype']=='bank':
				q="select * from bank where login_id='%s'"%(lid)
				res=select(q)
				print(q)
				if res:
					session['bank_id']=res[0]['bank_id']
					print(session['bank_id'])		
				return redirect(url_for('bank.bank_home'))


			elif res[0]['usertype']=='employee':
				q="select * from employee where login_id='%s'"%(lid)
				res=select(q)
				if res:
					session['employee_id']=res[0]['employee_id']
				return redirect(url_for('employee.employee_home'))

			elif res[0]['usertype']=='customer':
				q="select * from customer where login_id='%s'"%(lid)
				res=select(q)
				if res:
					session['customer_id']=res[0]['customer_id']
				return redirect(url_for('customer.customer'))



		return redirect(url_for('public.login'))

	return render_template('login.html')

