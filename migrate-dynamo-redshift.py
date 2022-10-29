import json
import boto3
import urllib.parse
import botocore.session as bc
import time
def migration_redshift(event, context):
    secret_name='staging-redshift-secret' ## HERE add the secret name created.
    session = boto3.session.Session()
    region = session.region_name
    client = session.client(
            service_name='secretsmanager',
            region_name=region
        )
    get_secret_value_response = client.get_secret_value(
            SecretId=secret_name
        )
    secret_arn=get_secret_value_response['ARN']
    secret = get_secret_value_response['SecretString']
    secret_json = json.loads(secret)
    cluster_id=secret_json['dbClusterIdentifier']
    bc_session = bc.get_session()
    session = boto3.Session(
            botocore_session=bc_session,
            region_name=region,
        )
    # Setup the client
    client_redshift = session.client("redshift-data")
    session = boto3.session.Session()
    dynamodb = session.resource('dynamodb')
    table = dynamodb.Table('user_txns')
    query_str2 = "INSERT into user_txns  values "
    query_str2_subset = ('({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}),')
    response = table.scan(Limit=150)
    data = response['Items']
    query_str_insert_final = query_str2
    query_str_insert_final = getInsertQuery(data,query_str2_subset,query_str_insert_final);
    query_str_insert_final = query_str_insert_final[:-1];
    response_query = client_redshift.execute_statement(Database= 'user_txns',SecretArn= secret_arn, Sql= query_str_insert_final, ClusterIdentifier= cluster_id)
    while 'LastEvaluatedKey' in response:
        query_str_insert_final=query_str2
        response = table.scan(ExclusiveStartKey=response['LastEvaluatedKey'],Limit=150)
        query_str_insert_final = getInsertQuery(response['Items'],query_str2_subset,query_str_insert_final)
        data.extend(response['Items'])
        query_str_insert_final = query_str_insert_final[:-1];
        response_query=client_redshift.execute_statement(Database= 'user_txns',SecretArn= secret_arn, Sql= query_str_insert_final, ClusterIdentifier= cluster_id)

def getInsertQuery(data,query_str2_subset,query_str_insert_final):
    for dataRecord in data:
        query_str_insert_final = query_str_insert_final+insertQuery(dataRecord,query_str2_subset)
    return query_str_insert_final

def insertQuery(dataRecord,query_str2_subset):
    nativeAmountReceived = 'Default'
    nativeCurrencyReceived = 'Default'
    amountReceived = 'Default'
    currencyReceived = 'Default'
    nativeAmountSent = 'Default'
    nativeCurrencySent = 'Default'
    amountSent = 'Default'
    currencySent = 'Default'
    feeType = 'Default'
    feeCurrency = 'Default'
    feeAmount = 'Default'
    feeResourceType = 'Default'

    for innerFeePart in dataRecord["fees"]:
        if("amount" in innerFeePart):
            feeAmount = innerFeePart["amount"]
        else:
            feeAmount = 'Default'
        if("currency" in innerFeePart and innerFeePart['currency']!=None):
            feeCurrency = "'"+innerFeePart["currency"]+"'"
        else:
            feeCurrency = 'Default'
        if("type" in innerFeePart and innerFeePart['type']!=None):
            feeType = "'"+innerFeePart["type"]+"'"
        else:
            feeType = 'Default'
        if("resourceType" in innerFeePart and innerFeePart['resourceType']!=None):
            feeResourceType = "'"+innerFeePart["resourceType"]+"'"
        else:
            feeResourceType = 'Default'

    for innerPart in dataRecord["parts"]:
        if(innerPart["direction"] == "SENT"):
            nativeCurrencyAndAmount = innerPart["nativeCurrencyAndAmount"]
            if("USD" in nativeCurrencyAndAmount):
                nativeAmountSent = nativeCurrencyAndAmount["USD"]["nativeAmount"]
                nativeCurrencySent = "'USD'"
            amountSent = innerPart["amount"]
            currencySent = innerPart["currency"]
            if(currencySent == "USD" ):
                nativeAmountSent = amountSent
                nativeCurrencySent = "'"+currencySent+"'"
        else:
            nativeCurrencyAndAmount = innerPart["nativeCurrencyAndAmount"]
            if("USD" in nativeCurrencyAndAmount):
                nativeAmountReceived = nativeCurrencyAndAmount["USD"]["nativeAmount"]
                nativeCurrencyReceived = "'USD'"
            amountReceived = innerPart["amount"]
            currencyReceived = innerPart["currency"];
            if(currencyReceived == "USD"):
                nativeAmountReceived = amountReceived
                nativeCurrencyReceived = "'"+currencyReceived+"'"

    if ("userTxnCategory" in dataRecord):
        userTxnCategoryValue = "'"+dataRecord["userTxnCategory"]+"'"
    else:
        userTxnCategoryValue = 'Default'
    if ("orderId" in dataRecord):
        orderIdValue = "'"+dataRecord["orderId"]+"'"
    else:
        orderIdValue = "\'randomId\'"
    if ("wallet" in dataRecord):
        walletValue = "'"+dataRecord["wallet"]+"'"
    else:
        walletValue = 'Default'
    if("txnHash" in dataRecord):
        txnHash = "'"+dataRecord["txnHash"]+"'"
    else:
        txnHash = 'Default'

    if("confirmedAt" in dataRecord):
        confirmedAtValue= "'"+dataRecord["confirmedAt"]+"'"
    else:
        confirmedAtValue= 'Default'


    return query_str2_subset.format("'"+str(dataRecord["userId"])+dataRecord["account"]+dataRecord["txnId"]+"'",
    dataRecord['userId'],
    "'"+dataRecord['txnId']+"'",
    "'"+dataRecord['vendor']+"'",
    "'"+dataRecord['account']+"'",
    "'"+dataRecord['uniqueAccountName']+"'",
    "'"+dataRecord['uniqueAccountNameAndTxnId']+"'",
    confirmedAtValue,
    "'"+dataRecord['txnDate']+"'",
    walletValue,
    "'"+dataRecord['txnStatus']+"'",
    "'"+dataRecord['txnType']+"'",
    "'"+dataRecord['txnCategory']+"'",
    txnHash,
    userTxnCategoryValue,
    orderIdValue,
    "'"+currencySent+"'",
    amountSent,
    nativeCurrencySent,
    nativeAmountSent,
    "'"+currencyReceived+"'",
    amountReceived,
    nativeCurrencyReceived,
    nativeAmountReceived,
    feeType,feeCurrency,feeAmount,feeResourceType
    )