from flask import *
from database import *
import uuid
from imageemail import *
import os

api=Blueprint('api',__name__)

@api.route("/login")
def login():
    data = {}

    uname = request.args['username']
    pwd = request.args['password']

    q = "SELECT * FROM login WHERE BINARY username='%s' AND BINARY password='%s'" % (uname, pwd)
    res = select(q)
    if res:
        data['status'] = 'success'
        data['data'] = res
    else:
        data['status'] = 'failed'
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
		q="update account set balance+'%s' where account_id='%s'"%(amount,acid)
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

    q="insert into complaint (customer_id,complaint,replay,date) values ('%s','%s','pending',curdate())"%(lid,complaint)
    res=insert(q)
    if res:
        data['status']="success"
        data['method']="addcomplaint"
    return str(data)
  
    
@api.route('/viewcomplaint')
def viewcomplaint():
    data={}
    lid=request.args['lid']
    z="select * from complaint where customer_id= '%s'"%(lid)
    res=select(z)

    if res:
        data['status']='success'
        data['data']=res
    else:
        data['status']='failed'
    data['method']="viewcomplaint"
    return str(data)







# ---------------------------secure moneytransfer-----------------------------




@api.route('/merchant_register',methods=['GET','POST'])
def merchant_register():
    data={}
    
    name=request.args['name']
    phone=request.args['phone']
    email=request.args['email']
    address=request.args['address']
    username=request.args['username']
    password=request.args['password']
    s="select username from login where username='%s'"%(username)
    res1=select(s)
    if res1:
        data['status']="exist"
        print("user exist llllllllllllllllllllll")
    else:
        s="insert into login values(null,'%s','%s','merchant')"%(username,password)
        login_id = insert(s)
        s="insert into merchant values(null,'%s','%s','%s','%s','%s')"%(login_id,name,address,phone,email)
        res=insert(s)
        print(res)
        print(s)
        if res:
            data['status']="success"
            data['data']=res
        else:
            data['status']="failed"
    return  str(data)



@api.route('/merchant_insert_wallet', methods=['GET', 'POST'])
def merchant_insert_wallet():
    data = {}
    username = request.args['username']
    amount = request.args['amount']

    # Query to check if the user exists
    s = "SELECT * FROM customer INNER JOIN login USING(login_id) WHERE username='%s'" % (username)
    data['check'] = select(s)

    if data['check']:
        # If user exists, insert the amount into the wallet
        user_id = data['check'][0]['customer_id']
        q = "INSERT INTO wallet VALUES (NULL, '%s', '%s')" % (user_id, amount)
        res = insert(q)
        print(q)
        
        if res:
            data['status'] = "Added"
            data['data'] = res
            data['method'] = 'category'
        else:
            data['status'] = "Failed"
    else:
        data['status'] = "User not found"

    return str(data)



@api.route('/view_wallet',methods=['GET','POST'])
def view_wallet():
    data={}
    
    s="select * from wallet inner join customer using(customer_id) inner join login using (login_id)"
    res=select(s)
    if res:
        data['data']=res
        data['status']="Success"
        data['method']='view_category'
    else:
        data['status']="Failed"
    return str(data)


@api.route('/delete_wallet',methods=['GET','POST'])
def delete_wallet():
    
    wallet_id=request.args['wallet_id']
    q="delete from wallet where wallet_id='%s'"%(wallet_id)
    delete(q)
    
    
    
@api.route('/update_wallet',methods=['GET','POST'])
def update_wallet():
    data={}
    wallet_id=request.args['wallet_id']
    # username=request.form['username']
    amount=request.args['amount']
    # s="select * from user inner join login using(login_id) where username='%s'"%(username)
    # data['check']=select(s)
    # if data['check']:
        # user_id=data['check'][0]['user_id']
    q="update wallet set amount='%s' where wallet_id='%s'"%(amount,wallet_id)
    update(q)
    return str(data)


@api.route('/view_transactions', methods=['GET', 'POST'])
def view_transactions():
    data = {"method": "view_transactions"}
    login_id = request.args['login_id']

    # Get account number of the logged-in user
    s = "SELECT account_number FROM account WHERE customer_id='%s'" % login_id
    res = select(s)

    if not res:
        data['status'] = "failed"
        return str(data)

    accno = res[0]['account_number']

    # Fetch transactions involving the user's account
    query = f"""
        SELECT 
            transaction_id,
            CASE 
                WHEN to_ac = '{accno}' THEN 'credited'
                ELSE 'debited'
            END AS transaction_direction,
            CASE 
                WHEN to_ac = '{accno}' THEN from_ac 
                ELSE to_ac 
            END AS other_account,
            amount,
            type,
            date_time
        FROM transaction 
        WHERE to_ac = '{accno}' OR from_ac = '{accno}'
    """
    res = select(query)

    if res:
        data['data'] = res
        data['status'] = "success"
    else:
        data['status'] = "failed"

    return str(data)


@api.route("/searchuser")
def searchuser():
	data={}


	num="%"+request.args['num']+"%"	
	q="select * from customer  where phone like '%s'"%(num)
	# print(num)
	res=select(q)
	if res:
		data['status']='success'
		data['data']=res
	else:
		data['status']="failed"	
		data['data']=res
	data['method']='searchuser'
	return str(data)


@api.route('/view_notification',methods=['GET','POST'])
def view_notification():
    data={}
    s="select * from notification"
    res=select(s)
    if res:
        data['data']=res
        data['status']="success"

    else:
        data['status']="Failed"
    data['method']='view_notification'
    return str(data)



@api.route('/view_payment',methods=['GET','POST'])
def view_payment():
    data={}
    login_id=request.args['login_id']
    customer_id=request.args['customer_id']
    # print(customer_id)
    s="select * from login inner join customer where customer_id='%s'"%(customer_id)
    data['id']=select(s)
    cuslog=data['id'][0]['login_id']
    # print(login_id)
    # print(cuslog)
    s="SELECT * FROM transfer WHERE sender_id='%s' AND receiver_id='%s'  AND receivertype='merchant'"%(cuslog,login_id)
    res=select(s)
    if res:
        data['data']=res
        data['stat']="success"
        data['method']='view_payment'
    else:
        data['stat']="Failed"
        data['method']='view_payment'
    return str(data)



@api.route('/merchant_request_money',methods=['GET','POST'])
def merchant_request_money():
    data={}
    
    login_id=request.args['log_id']
    print(login_id)
    customer_id=request.args['customer_id']
    print(customer_id)
    s="select * from login inner join customer where customer_id='%s'"%(customer_id)
    data['id']=select(s)
    cuslog=data['id'][0]['login_id']
    amount=request.args['amount']
    s="insert into transfer values(null,'%s','customer','%s','merchant','%s',curdate(),'pending')"%(cuslog,login_id,amount)
    res=insert(s)
    if res:
        data['status']="success"
        data['data']=res
    else:
        data['status']="failed"
    return  str(data)




@api.route('/user_register',methods=['GET','POST'])
def user_register():
    data={}
    
    fname=request.args['fname']
    lname=request.args['lname']
    housename=request.args['housename']
    pincode=request.args['pincode']
    gender=request.args['gender']
    phone=request.args['phone']
    email=request.args['email']
    address=request.args['address']
    username=request.args['username']
    password=request.args['password']
    s="select username from login where username='%s'"%(username)
    res1=select(s)
    if res1:
        data['status']="exist"
        print("user exist llllllllllllllllllllll")
    else:
        s="insert into login values(null,'%s','%s','customer')"%(username,password)
        login_id = insert(s)
        s="insert into customer values(null,'%s','%s','%s','%s','%s','%s','%s','%s','%s')"%(login_id,fname,lname,housename,address,pincode,phone,email,gender)
        res=insert(s)
        q = "INSERT INTO wallet VALUES (NULL, '%s',0,'customer')" % (login_id)
        insert(q)
        print(res)
        print(s)
        if res:
            data['status']="success"
            data['data']=res
        else:
            data['status']="failed"
    return  str(data)



@api.route('/user_view_transactions',methods=['GET','POST'])
def user_view_transactions():

    data = {"method": "view_transactions"}
    login_id = request.args['login_id']

    # Get account number of the logged-in user
    s = "SELECT account_number FROM account WHERE customer_id='%s'" % login_id
    res = select(s)

    if not res:
        data['status'] = "failed"
        return str(data)

    accno = res[0]['account_number']

    # Fetch transactions involving the user's account
    query = f"""
        SELECT 
            transaction_id,
            CASE 
                WHEN to_ac = '{accno}' THEN 'credited'
                ELSE 'debited'
            END AS transaction_direction,
            CASE 
                WHEN to_ac = '{accno}' THEN from_ac 
                ELSE to_ac 
            END AS other_account,
            amount,
            type,
            date_time
        FROM transaction 
        WHERE to_ac = '{accno}' OR from_ac = '{accno}'
    """
    res = select(query)

    if res:
        data['data'] = res
        data['status'] = "success"
    else:
        data['status'] = "failed"

    return str(data)

@api.route('/merchant_wallet', methods=['GET', 'POST'])
def merchant_wallet():
    data = {}
    login_id = request.args['login_id']
    amount = request.args['amount']

    # Fetch merchant details
    s = "SELECT * FROM login INNER JOIN merchant USING(login_id) WHERE login_id='%s'" % (login_id)
    data['id'] = select(s)
    # merchant_id = data['id'][0]['merchant_id']
    type = data['id'][0]['usertype']
    # Check if wallet entry exists
    s = "SELECT * FROM wallet WHERE customer_id='%s' AND customer_type='%s'" % (login_id, type)
    data['check'] = select(s)

    if float(amount) == 0:  # Only fetch balance
        if data['check']:
            data['status'] = "Added"
            data['data'] = data['check']
        else:
            data['status'] = "No wallet entry found"
            data['data'] = []
    else:
        # Perform update or insert
        if data['check']:
            u = "UPDATE wallet SET amount = amount + '%s' WHERE customer_id='%s' AND customer_type='%s'" % (amount, login_id, type)
            res = update(u)
        else:
            q = "INSERT INTO wallet VALUES (NULL, '%s', '%s', '%s')" % (login_id, amount, type)
            res = insert(q)

        # Fetch the updated wallet balance
        s = "SELECT amount FROM wallet WHERE customer_id='%s' AND customer_type='%s'" % (login_id, type)
        updated_balance = select(s)

        if res and updated_balance:
            data['status'] = "Added"
            data['data'] = updated_balance
        else:
            data['status'] = "Failed"

    return str(data)


@api.route('/merchant_withdraw_money', methods=['GET', 'POST'])
def merchant_withdraw_money():
    data = {}
    try:
        login_id = request.args.get('login_id')
        amount = request.args.get('amount')

        # Validate inputs
        if not login_id or not amount:
            data['status'] = "error"
            data['message'] = "Login ID and amount are required."
            return jsonify(data)

        try:
            # Convert amount to a numeric value
            amount = float(amount)
            if amount <= 0:
                data['status'] = "error"
                data['message'] = "Invalid amount. Amount must be greater than 0."
                return jsonify(data)
        except ValueError:
            data['status'] = "error"
            data['message'] = "Invalid amount format."
            return jsonify(data)

        # Fetch wallet balance
        s = "SELECT * FROM wallet WHERE customer_id='%s'" % (login_id)
        wallet = select(s)

        if not wallet:
            data['status'] = "error"
            data['message'] = "Wallet not found for the given Login ID."
            return jsonify(data)

        balance = float(wallet[0]['amount'])

        # Check if sufficient balance is available
        if balance >= amount:
            # Update the wallet balance
            u = "UPDATE wallet SET amount = amount - '%s' WHERE customer_id='%s'" % (amount, login_id)
            update(u)
            # u="insert into transaction values(null,'%s','merchant','bank','%s',curdate())"%(login_id,amount)
            # insert(u)
            u="update account set balance=balance+'%s' where customer_id='%s'"%(amount,login_id)
            update(u)
            data['status'] = "success"
            data['message'] = "Withdrawal successful."
            data['remaining_balance'] = balance - amount
        else:
            data['status'] = "error"
            data['message'] = "Insufficient balance."
            data['available_balance'] = balance

    except Exception as e:
        data['status'] = "error"
        data['message'] = f"An error occurred: {str(e)}"

    return jsonify(data)
    




@api.route('/user_wallet', methods=['GET', 'POST'])
def user_wallet():
    data = {}
    login_id = request.args['login_id']
    amount = float(request.args['amount'])
    action = request.args.get('action', 'fetch')  # 'fetch', 'add', or 'withdraw'

    # Fetch user details
    user_query = """
        SELECT * FROM login 
        INNER JOIN customer USING(login_id) 
        WHERE login_id='%s'
    """ % (login_id)
    user_details = select(user_query)

    if not user_details:
        data['status'] = "Invalid user"
        return str(data)

    customer_type = user_details[0]['usertype']
    customer_id = login_id  # Assuming customer_id is the same as login_id for simplicity.

    # Check if wallet entry exists
    wallet_query = """
        SELECT * FROM wallet 
        WHERE customer_id='%s' AND customer_type='%s'
    """ % (customer_id, customer_type)
    wallet_details = select(wallet_query)

    # Fetch current bank balance
    bank_query = "SELECT balance FROM account WHERE customer_id='%s'" % (customer_id)
    bank_details = select(bank_query)
    bank_balance = float(bank_details[0]['balance']) if bank_details else 0.0

    if action == 'fetch':
        # Fetch wallet balance
        if wallet_details:
            data['status'] = "fetch"
            data['data'] = wallet_details
        else:
            data['status'] = "No wallet entry found"
            data['data'] = []

    elif action == 'add':
        # Adding money to the wallet
        if bank_balance < amount:
            data['status'] = "Insufficient bank balance"
        else:
            if wallet_details:
                # Update wallet balance
                update_wallet_query = """
                    UPDATE wallet 
                    SET amount = amount + '%s' 
                    WHERE customer_id='%s' AND customer_type='%s'
                """ % (amount, customer_id, customer_type)
                update(update_wallet_query)
            else:
                # Create a new wallet entry
                insert_wallet_query = """
                    INSERT INTO wallet VALUES (NULL, '%s', '%s', '%s')
                """ % (customer_id, amount, customer_type)
                insert(insert_wallet_query)

            # Deduct from bank balance
            update_bank_query = """
                UPDATE account 
                SET balance = balance - '%s' 
                WHERE customer_id='%s'
            """ % (amount, customer_id)
            update(update_bank_query)

            # Fetch updated wallet balance
            updated_wallet_query = """
                SELECT amount FROM wallet 
                WHERE customer_id='%s' AND customer_type='%s'
            """ % (customer_id, customer_type)
            updated_wallet = select(updated_wallet_query)

            data['status'] = "Added"
            data['data'] = updated_wallet

    elif action == 'withdraw':
        # Withdrawing money from the wallet
        if wallet_details:
            wallet_balance = float(wallet_details[0]['amount'])
            if wallet_balance < amount:
                data['status'] = "Insufficient wallet balance"
            else:
                # Deduct from wallet
                update_wallet_query = """
                    UPDATE wallet 
                    SET amount = amount - '%s' 
                    WHERE customer_id='%s' AND customer_type='%s'
                """ % (amount, customer_id, customer_type)
                update(update_wallet_query)

                # Add to bank balance
                update_bank_query = """
                    UPDATE account 
                    SET balance = balance + '%s' 
                    WHERE customer_id='%s'
                """ % (amount, customer_id)
                update(update_bank_query)

                # Fetch updated wallet balance
                updated_wallet_query = """
                    SELECT amount FROM wallet 
                    WHERE customer_id='%s' AND customer_type='%s'
                """ % (customer_id, customer_type)
                updated_wallet = select(updated_wallet_query)

                data['status'] = "Withdrawn"
                data['data'] = updated_wallet
        else:
            data['status'] = "No wallet entry found"

    else:
        data['status'] = "Invalid action"

    return str(data)



@api.route("/user_search")
def user_search():
    data = {}
    num = "%" + request.args['num'] + "%"
    q = """
    SELECT login_id AS user_id, 'customer' AS user_type, phone, f_name, l_name FROM customer inner join login using(login_id) WHERE phone LIKE '%s'
    UNION
    SELECT login_id AS user_id, 'merchant' AS user_type, phone, merchantname AS f_name, '' AS l_name FROM merchant inner join login using(login_id)  WHERE phone LIKE '%s'
    """%(num,num)

    res = select(q)

    if res:
        data['status'] = 'success'
        data['data'] = res
    else:
        data['status'] = 'failed'
        data['data'] = []

    data['method'] = 'user_search'
    return str(data)




@api.route('/user_request_money',methods=['GET','POST'])
def user_request_money():
    data={}
    
    login_id=request.args['log_id']
    customer_id=request.args['customer_id']
    # s="select * from login inner join customer where customer_id='%s'"%(customer_id)
    # data['id']=select(s)
    # cuslog=data['id'][0]['login_id']
    amount=request.args['amount']
    s="insert into transfer values(null,'%s','customer','%s','customer','%s',curdate(),'pending')"%(customer_id,login_id,amount)
    res=insert(s)
    if res:
        data['status']="success"
        data['data']=res
    else:
        data['status']="failed"
    return  str(data)



@api.route('/user_view_payment',methods=['GET','POST'])
def user_view_payment():
    data={}
    login_id=request.args['login_id']
    customer_id=request.args['customer_id']
    # print(customer_id)
    # s="select login_id from login inner join customer where customer_id='%s' union select login_id from login inner join merchant where merchant_id='%s'"%(customer_id,customer_id)
    # data['id']=select(s)
    # cuslog=data['id'][0]['login_id']
    # print(login_id)
    # print(cuslog)
    print(customer_id)
    s="SELECT * FROM transfer WHERE sender_id='%s' AND receiver_id='%s' OR sender_id='%s' AND receiver_id='%s' "%(customer_id,login_id,login_id,customer_id)
    res=select(s)
    if res:
        data['data']=res
        data['stat']="success"
    else:
        data['stat']="Failed"
    data['method']='view_payment'
    return str(data)



@api.route('/user_qr_pay', methods=['GET', 'POST'])
def user_qr_pay():
    data = {}

    try:
        # Extract parameters from request
        sender_id = request.args.get('sender_id')
        receiver_id = request.args.get('receiver_id')
        amount = request.args.get('amount')

        # Debugging logs
        print("Sender ID:", sender_id)
        print("Receiver ID:", receiver_id)
        print("Amount:", amount)

        # Validation for missing parameters
        if not sender_id or not receiver_id or not amount:
            data['status'] = "failed"
            data['message'] = "Missing sender_id, receiver_id, or amount"
            return str(data)

        # Validate amount is a positive number
        try:
            amount = float(amount)
            if amount <= 0:
                data['status'] = "failed"
                data['message'] = "Amount must be greater than zero"
                return str(data)
        except ValueError:
            data['status'] = "failed"
            data['message'] = "Invalid amount format"
            return str(data)

        # Fetch sender and receiver types
        sender_type_query = f"SELECT usertype AS sendertype FROM login WHERE login_id='{sender_id}'"
        sender_data = select(sender_type_query)
        if not sender_data:
            data['status'] = "failed"
            data['message'] = "Invalid sender_id"
            return str(data)

        receiver_type_query = f"SELECT usertype AS receivertype FROM login WHERE login_id='{receiver_id}'"
        receiver_data = select(receiver_type_query)
        if not receiver_data:
            data['status'] = "failed"
            data['message'] = "Invalid receiver_id"
            return str(data)

        sendertype = sender_data[0]['sendertype']
        receivertype = receiver_data[0]['receivertype']

        # Check sender's wallet balance
        wallet_query = f"SELECT CAST(amount AS DECIMAL(10, 2)) AS balance FROM wallet WHERE customer_id='{sender_id}'"
        wallet_data = select(wallet_query)
        if not wallet_data or float(wallet_data[0]['balance']) < amount:
            data['status'] = "failed"
            data['message'] = "Insufficient wallet balance"
            return str(data)

        # Deduct amount from sender's wallet
        update_sender_wallet_query = f"UPDATE wallet SET amount = amount - {amount} WHERE customer_id='{sender_id}'"
        update(update_sender_wallet_query)

        # Add amount to receiver's wallet
        update_receiver_wallet_query = f"UPDATE wallet SET amount = amount + {amount} WHERE customer_id='{receiver_id}'"
        update(update_receiver_wallet_query)

        # Insert into transfer table
        transfer_query = f"""
            INSERT INTO transfer 
            VALUES (NULL, '{sender_id}', '{sendertype}', '{receiver_id}', '{receivertype}', {amount}, CURDATE(), 'paid')
        """
        transfer_id = insert(transfer_query)

        # Insert into payment table
        payment_query = f"INSERT INTO payment VALUES (NULL, '{transfer_id}', {amount}, CURDATE())"
        insert(payment_query)

        # Success response
        data['status'] = "success"
        data['transfer_id'] = transfer_id
    except Exception as e:
        # Log and return error
        print("Error:", str(e))
        data['status'] = "failed"
        data['error'] = str(e)

    return str(data)




@api.route('/user_pay_request',methods=['GET','POST'])
def user_pay_request():
    data={}
    login_id=request.args['login_id']
    # customer_id=request.args['customer_id']
    # print(customer_id)
    # s="select login_id from login inner join customer where customer_id='%s' union select login_id from login inner join merchant where merchant_id='%s'"%(customer_id,customer_id)
    # data['id']=select(s)
    # cuslog=data['id'][0]['login_id']
    # print(login_id)
    # print(cuslog)
    # print(customer_id)
    s="SELECT * FROM transfer WHERE sender_id='%s' and status='pending' "%(login_id)
    res=select(s)
    if res:
        data['data']=res
        data['stat']="success"
        data['method']='view_payment'
    else:
        data['stat']="Failed"
        data['method']='view_payment'
    return str(data)


@api.route('/user_send_money', methods=['GET', 'POST'])
def user_send_money():
    data = {}
    try:
        transfer_id = request.args.get('transfer_id')
        amt = request.args.get('amount')

        # Validate parameters
        if not transfer_id or not amt or not amt.isdigit() or float(amt) <= 0:
            data['status'] = "failed"
            data['message'] = "Invalid transfer_id or amount"
            return str(data)

        amt = float(amt)

        # Check if already paid
        check_payment_query = f"SELECT * FROM payment WHERE transfer_id='{transfer_id}'"
        if select(check_payment_query):
            data['status'] = "Already Paid"
            return str(data)

        # Get sender and receiver
        transfer_query = f"SELECT sender_id, receiver_id FROM transfer WHERE transfer_id='{transfer_id}'"
        transfer_data = select(transfer_query)
        if not transfer_data:
            data['status'] = "failed"
            data['message'] = "Invalid transfer ID"
            return str(data)
        sender_id = transfer_data[0]['sender_id']
        receiver_id = transfer_data[0]['receiver_id']
        print(receiver_id)
        # Check wallet balance
        wallet_query = f"SELECT CAST(amount AS DECIMAL(10, 2)) AS balance FROM wallet WHERE customer_id='{sender_id}'"
        wallet_data = select(wallet_query)
        if not wallet_data or float(wallet_data[0]['balance']) < amt:
            data['status'] = "lowmoney"
            data['message'] = "Insufficient balance"
            return str(data)

        # Begin transaction
        update_transfer_query = f"UPDATE transfer SET status='paid' WHERE transfer_id='{transfer_id}'"
        insert_payment_query = f"INSERT INTO payment VALUES (NULL, '{transfer_id}', '{amt}', CURDATE())"
        deduct_wallet_query = f"UPDATE wallet SET amount = amount - {amt} WHERE customer_id='{sender_id}'"
        add_wallet_query = f"UPDATE wallet SET amount = amount + {amt} WHERE customer_id='{receiver_id}'"

        update(update_transfer_query)
        insert(insert_payment_query)
        update(deduct_wallet_query)
        update(add_wallet_query)

        # Success response
        data['status'] = "success"
        data['transfer_id'] = transfer_id
    except Exception as e:
        print("Error:", str(e))
        data['status'] = "failed"
        data['error'] = str(e)
    return str(data)







@api.route('/get_recipient_info', methods=['GET'])
def get_recipient_info():
    data = {}
    try:
        transfer_id = request.args.get('transfer_id')
        
        # Validate parameters
        if not transfer_id:
            data['status'] = "failed"
            data['message'] = "Invalid transfer_id"
            return str(data)
            
        # Get transfer details
        transfer_query = f"SELECT sender_id, receiver_id FROM transfer WHERE transfer_id='{transfer_id}'"
        transfer_data = select(transfer_query)
        
        if not transfer_data:
            data['status'] = "failed"
            data['message'] = "Invalid transfer ID"
            return str(data)
            
        receiver_id = transfer_data[0]['receiver_id']
        
        # Get recipient's name from either customer or merchant table
        recipient_query = f"""
        SELECT 
            CASE 
            WHEN login.usertype = 'customer' THEN CONCAT(customer.f_name, ' ', customer.l_name)
            WHEN login.usertype = 'merchant' THEN merchant.merchantname
            ELSE 'Unknown'
            END AS name
        FROM login
        LEFT JOIN customer ON login.login_id = customer.login_id
        LEFT JOIN merchant ON login.login_id = merchant.login_id
        WHERE login.login_id = '{receiver_id}'
        """
        recipient_data = select(recipient_query)
        if not recipient_data:
            data['status'] = "failed"
            data['message'] = "Recipient not found"
            return str(data)
            
        data['status'] = "success"
        data['recipient_name'] = recipient_data[0]['name']
        data['recipient_id'] = receiver_id
        
    except Exception as e:
        print("Error:", str(e))
        data['status'] = "failed"
        data['error'] = str(e)
        
    return str(data)




@api.route('/fetch_account_details', methods=['GET','POST'])
def fetch_account_details():
    data={}
    customer_id = request.args['customer_id']
    query = f"SELECT account_number, ifsc FROM account WHERE customer_id='{customer_id}'"
    result = select(query)
    if result:
        data['status'] = "success"
        data['account_details'] = result
    else:
        data['status'] = "failed"
        data['account_details'] = ''
    return str(data)


@api.route('/add_account_details', methods=['GET','POST'])
def add_account_details():
    customer_id = request.args.get('customer_id')
    account_number = request.args.get('account_number')
    ifsc = request.args.get('ifsc')
    query = f"INSERT INTO account (customer_id, account_number, ifsc,balance,date_time) VALUES ('{customer_id}', '{account_number}', '{ifsc}',5000,now())"
    insert(query)
    return jsonify({"status": "success"})


@api.route('/update_account_details', methods=['GET','POST'])
def update_account_details():
    customer_id = request.args.get('customer_id')
    account_number = request.args.get('account_number')
    ifsc = request.args.get('ifsc')
    query = f"UPDATE account SET account_number='{account_number}', ifsc='{ifsc}',date_time= now()  WHERE customer_id='{customer_id}'"
    update(query)
    return jsonify({"status": "success"})


@api.route('/transfer_money', methods=['GET', 'POST'])
def transfer_money():
    # Get query parameters
    login_id = request.args.get('login_id')
    to_ac = request.args.get('account_number')
    to_ifsc = request.args.get('ifsc')
    amount = float(request.args.get('amount'))

    # Fetch sender's account details
    s = "SELECT * FROM account WHERE customer_id='%s'" % (login_id)
    res = select(s)
    
    if not res:  # Check if the account exists
        return jsonify({"status": "failed", "message": "Account not found"})
    
    balance = float(res[0]['balance'])
    if balance < amount:
        return jsonify({"status": "insufficient_balance"})

    # Deduct the amount from sender's account
    u = "UPDATE account SET balance=balance-'%s' WHERE customer_id='%s'" % (amount, login_id)
    update(u)
    
    from_ac = res[0]['account_number']
    from_ifsc = res[0]['ifsc']

    # Insert transaction record
    query = (
        f"INSERT INTO transaction (from_ac, from_ifsc, to_ac, to_ifsc, amount, date_time, customer_id, type) "
        f"VALUES ('{from_ac}', '{from_ifsc}', '{to_ac}', '{to_ifsc}', '{amount}', CURDATE(), '{login_id}', 'online')"
    )
    insert(query)

    # Fetch the customer ID of the receiver
    s = "SELECT customer_id FROM account WHERE account_number='%s' and ifsc='%s'"  % (to_ac,to_ifsc)
    res = select(s)
    if not res:  # Check if the recipient's account exists
        return jsonify({"status": "failed", "message": "Recipient account not found"})

    recipient_id = res[0]['customer_id']

    # Add the amount to the recipient's account
    u = "UPDATE account SET balance=balance+'%s' WHERE customer_id='%s'" % (amount, recipient_id)
    update(u)

    return jsonify({"status": "success", "message": "Transfer successful"})

@api.route('/wallet_transactions', methods=['GET', 'POST'])
def wallet_transactions():
    data = {}
    login_id = request.args['login_id']

    # Modified query to handle both customer and merchant tables
    s = f"""
        SELECT 
            transfer_id,
            amount,
            status,
            CASE 
                WHEN sender_id = '{login_id}' THEN 'debited'
                ELSE 'credited'
            END AS transaction_direction,
            CASE 
                WHEN sender_id = '{login_id}' THEN 
                    COALESCE(receiver_customer.f_name, receiver_merchant.merchantname) 
                ELSE 
                    sender_customer.f_name
            END AS other_party_name,
            transfer.date
        FROM transfer
        LEFT JOIN customer AS sender_customer ON transfer.sender_id = sender_customer.login_id
        LEFT JOIN customer AS receiver_customer ON transfer.receiver_id = receiver_customer.login_id
        LEFT JOIN merchant AS receiver_merchant ON transfer.receiver_id = receiver_merchant.login_id
        WHERE (sender_id = '{login_id}' OR receiver_id = '{login_id}') AND status = 'paid'
    """
    res = select(s)

    if res:
        data['data'] = res
        data['stat'] = "success"
        data['method'] = "view_payment"
    else:
        data['stat'] = "failed"
        data['method'] = "view_payment"

    return str(data)




@api.route('/wallet_notification_receiver', methods=['GET', 'POST'])
def wallet_notification_receiver():
    data={}
    login_id=request.args['login_id']
    
    
    return str(data)






#--------------------------------------------------------------------------------------------
# Flask API (Python)

@api.route('/api/uploadencryptedimages', methods=['POST'])
def upload_encrypted_images():
    try:
        # Validate the request to ensure it contains files and form data
        if not request.files or not request.form:
            return jsonify({
                'status': 'failure',
                'message': 'Invalid request format'
            }), 400

        # Extract customer_id and log_id from the form data
        customer_id = request.form.get('customer_id')
        log_id = request.form.get('log_id')

        # Check if both customer_id and log_id are provided
        if not all([customer_id, log_id]):
            return jsonify({
                'status': 'failure',
                'message': 'Missing required parameters'
            }), 400

        # Extract the two image files (share1 and share2) from the request
        share1_file = request.files.get('share1')
        share2_file = request.files.get('share2')

        # Check if both image files are provided
        if not all([share1_file, share2_file]):
            return jsonify({
                'status': 'failure',
                'message': 'Missing image files'
            }), 400

        # Validate that the uploaded files are PNG images
        if not all(f.content_type == 'image/png' for f in [share1_file, share2_file]):
            return jsonify({
                'status': 'failure',
                'message': 'Invalid file type. Only PNG images are allowed'
            }), 400

        # Define the directory to save the uploaded images
        upload_dir = os.path.join(current_app.root_path, "static", "qrcode")
        os.makedirs(upload_dir, exist_ok=True)  # Create the directory if it doesn't exist

        # Generate unique filenames for the uploaded images
        share1_filename = secure_filename(f"share1_{customer_id}_{log_id}_{uuid.uuid4()}.png")
        share2_filename = secure_filename(f"share2_{customer_id}_{log_id}_{uuid.uuid4()}.png")

        # Define the full paths for saving the images
        share1_path = os.path.join(upload_dir, share1_filename)
        share2_path = os.path.join(upload_dir, share2_filename)

        # Save the uploaded images to the specified paths
        share1_file.save(share1_path)
        share2_file.save(share2_path)

        # Return a success response with the paths of the uploaded images
        return jsonify({
            'status': 'success',
            'message': 'Images uploaded successfully',
            'share1_path': f"/static/qrcode/{share1_filename}",
            'share2_path': f"/static/qrcode/{share2_filename}"
        }), 200

    except Exception as e:
        # Handle any server errors and return an error response
        return jsonify({
            'status': 'failure',
            'message': f'Server error: {str(e)}'
        }), 500


@api.route('uploadfile2', methods=['post'])
def uploadfile2():
    # Extract customer ID from the form data
    lid = request.form["lid"]
    # Query to fetch customer details based on customer ID
    q = "select * from customer where customer_id='%s'" % (lid)
    res = select(q)

    # Import the Image module from PIL for image processing
    from PIL import Image

    # Extract the two image files from the request
    image = request.files["image"]
    image1 = request.files["image1"]

    # Define the directory to save the uploaded images
    upload_dir = r"static\qrcode"
    os.makedirs(upload_dir, exist_ok=True)  # Create the directory if it doesn't exist

    # Generate unique paths for saving the images
    path = os.path.join("static/qrcode/" + str(uuid.uuid4()) + image.filename)
    path1 = os.path.join("static/qrcode/" + str(uuid.uuid4()) + image1.filename)

    # Save the uploaded images to the specified paths
    image.save(path)
    image1.save(path1)

    # Insert the image paths into the database
    i = "insert into upload_file (customer_id,img1,img2) values('%s','%s','%s')" % (lid, path, path1)
    insert(i)

    # Return an empty JSON response
    return jsonify()


from PIL import Image
import uuid

@api.route('uploadfile', methods=['post'])
def uploadfile():
    # Extract customer ID from the form data
    lid = request.form["lid"]
    # Query to fetch customer details based on customer ID
    q = "select * from customer where customer_id='%s'" % (lid)
    res = select(q)

    # Extract the two image files from the request
    image = request.files["image"]
    image1 = request.files["image1"]

    # Define the directory to save the uploaded images
    upload_dir = r"static\qrcode/"

    # Generate a unique path for saving the first image
    path = os.path.join("static/qrcode/" + str(uuid.uuid4()) + image.filename)
    path1_dir = "static/"
    image.save(path)  # Save the first image

    # Define the path for the second image
    path1 = os.path.join("static/test1.jpg")
    filesssss(path1, res[0]['email'])  # Perform some operation with the second image
    image1.save(path1)  # Save the second image

    # Insert the first image path into the database
    i = "insert into upload_file (customer_id,img1) values('%s','%s')" % (lid, path)
    insert(i)

    # Return an empty JSON response
    return jsonify()


@api.route('upload_image', methods=['post'])
def upload_image():
    data = {}
    # Extract the image file from the request
    image = request.files["image"]
    print(image, '///////////////////')

    # Generate a unique path for saving the image
    path = "static/qrcode/" + str(uuid.uuid4()) + image.filename
    image.save(path)  # Save the image
    print(path, "jjjjjjjjjjjjjjjjjjjjjjjjjjjjj")

    # Extract the login ID from the form data
    log_id = request.form['log_id']
    # Query to fetch customer ID based on login ID
    s = "SELECT customer_id FROM login INNER JOIN customer USING(login_id) WHERE customer.login_id='%s'" % (log_id)
    print(s)
    res = select(s)
    customer_id = res[0]['customer_id']
    print("-----------------", customer_id)

    # Update the database with the new image path
    i = "update upload_file set img2='%s' where customer_id='%s'" % (path, customer_id)
    res = update(i)

    if res:
        # Fetch the updated image details from the database
        q = "select * from upload_file where customer_id='%s' order by upload_file_id desc" % (customer_id)
        ress = select(q)
        print(ress)
        data['data'] = ress
        data['stat'] = "success"
    else:
        data['stat'] = "failed"

    return str(data)


@api.route('get_images', methods=['post'])
def get_images():
    data = {}
    # Extract the login ID from the form data
    log_id = request.form['log_id']
    # Query to fetch customer ID based on login ID
    s = "SELECT customer_id FROM login INNER JOIN customer USING(login_id) WHERE customer.login_id='%s'" % (log_id)
    print(s)
    res = select(s)
    customer_id = res[0]['customer_id']

    # Query to fetch image details for the customer
    q = "select * from upload_file where customer_id='%s' order by upload_file_id desc" % (customer_id)
    ress = select(q)
    print(ress)
    data['data'] = ress
    data['stat'] = "success"

    return str(data)


@api.route('/viewimages', methods=['GET'])
def viewimages():
    data = {}
    # Extract the login ID from the request arguments
    log_id = request.args['log_id']
    # Query to fetch customer ID based on login ID
    s = "SELECT customer_id FROM login INNER JOIN customer USING(login_id) WHERE customer.login_id='%s'" % (log_id)
    res = select(s)
    if res:
        customer_id = res[0]['customer_id']
        # Query to fetch image details for the customer
        q = "SELECT * FROM upload_file WHERE customer_id='%s' ORDER BY upload_file_id DESC" % (customer_id)
        ress = select(q)
        if ress:
            data['data'] = ress
            data['stat'] = "success"
        else:
            data['stat'] = "failed"
    else:
        data['stat'] = "failed"
    return str(data)

