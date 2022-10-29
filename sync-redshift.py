import json
import urllib.parse
import boto3
import botocore.session as bc
s3 = boto3.client('s3')
def sync_redshift(event, context):
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

    print("Data API client successfully loaded")
    query_str2 = "INSERT into user_txns  values ";
    query_str2_subset = ('({},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}),');
    query_str_insert_final = query_str2+"";
    query_str_remove = "DELETE from \"user_txns\" where id in( ";
    query_str_remove_subset = '{},'
    query_str_remove_final =query_str_remove+"";
    for record in event['Records']:
        if(record['eventName'] == 'REMOVE'):
            query_str_remove_final = query_str_remove_final + getQueryStringForDelete(query_str_remove_subset,record)
        if(record['eventName'] == 'INSERT'):
            query_str_insert_final = query_str_insert_final+getQueryStringForInsert(query_str2_subset,record)
        if(record['eventName'] == 'MODIFY'):
            query_str_insert_final = query_str_insert_final+getQueryStringForInsert(query_str2_subset,record)
            query_str_remove_final = query_str_remove_final + getQueryStringForDelete(query_str_remove_subset,record);
            # print
    query_str_remove_final = query_str_remove_final[:-1];
    query_str_remove_final = query_str_remove_final+")"
    resp = client_redshift.execute_statement(Database = 'user_txns',SecretArn= secret_arn, Sql= query_str_remove_final,ClusterIdentifier= cluster_id)
    query_str_insert_final = query_str_insert_final[:-1];
    resp = client_redshift.execute_statement(Database= 'user_txns',SecretArn= secret_arn, Sql= query_str_insert_final, ClusterIdentifier= cluster_id)
def getQueryStringForDelete(queryString,record):
    oldImageRecord = record["dynamodb"]["OldImage"];
    return queryString.format("'"+oldImageRecord["userId"]["N"]+oldImageRecord["uniqueAccountName"]["S"]+oldImageRecord["txnId"]["S"]+"'")
def getQueryStringForInsert(queryString,record):
    newImageRecord = record["dynamodb"]["NewImage"]
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
    for feePart in newImageRecord["fees"]["L"]:
        innerFeePart = feePart["M"]
        if("NULL" in innerFeePart["amount"] and innerFeePart["amount"]["NULL"] == True):
            feeAmount = 'Default'
        else:
            feeAmount = innerFeePart["amount"]["N"]
        if("NULL" in innerFeePart["currency"] and innerFeePart["currency"]["NULL"] == True):
            feeCurrency = 'Default'
        else:
            feeCurrency = "'"+innerFeePart["currency"]["S"]+"'"
        if("NULL" in innerFeePart["type"] and innerFeePart["type"]["NULL"] == True):
            feeType = 'Default'
        else:
            feeType = "'"+innerFeePart["type"]["N"]+"'"
        if("NULL" in innerFeePart["resourceType"] and innerFeePart["resourceType"]["NULL"] == True):
            feeResourceType = 'Default'
        else:
            feeResourceType = "'"+innerFeePart["resourceType"]["S"]+"'"
    for part in newImageRecord["parts"]["L"]:
        innerPart = part["M"]
        # print(innerPart["nativeCurrency"]["S"])
        if(innerPart["direction"]["S"] == "SENT"):
            nativeCurrencyAndAmount = innerPart["nativeCurrencyAndAmount"]["M"]
            if("USD" in nativeCurrencyAndAmount):
                nativeAmountSent = nativeCurrencyAndAmount["USD"]["M"]["nativeAmount"]["N"]
                nativeCurrencySent = "'USD'"
            amountSent = innerPart["amount"]["N"]
            currencySent = innerPart["currency"]["S"]
            if(currencySent == "USD" ):
                nativeAmountSent = amountSent
                nativeCurrencySent = "'"+currencySent+"'"
        else:
            nativeCurrencyAndAmount = innerPart["nativeCurrencyAndAmount"]["M"]
            if("USD" in nativeCurrencyAndAmount):
                nativeAmountReceived = nativeCurrencyAndAmount["USD"]["M"]["nativeAmount"]["N"]
                nativeCurrencyReceived = "'USD'"
            amountReceived = innerPart["amount"]["N"]
            currencyReceived = innerPart["currency"]["S"];
            if(currencyReceived == "USD"):
                nativeAmountReceived = amountReceived
                nativeCurrencyReceived = "'"+currencyReceived+"'"
    if ("orderId" in newImageRecord):
        orderIdValue = "'"+newImageRecord["orderId"]["S"]+"'"
    else:
        orderIdValue = "\'randomId\'"
    if ("userTxnCategory" in newImageRecord):
        userTxnCategoryValue = "'"+newImageRecord["userTxnCategory"]["S"]+"'"
    else:
        userTxnCategoryValue = 'Default'
    if ("wallet" in newImageRecord):
        walletValue = "'"+newImageRecord["wallet"]["S"]+"'"
    else:
        walletValue = 'Default'
    if("txnHash" in newImageRecord):
        txnHash = "'"+newImageRecord["txnHash"]["S"]+"'"
    else:
        txnHash = 'Default'
    if("confirmedAt" in newImageRecord):
        confirmedAtValue = "'"+newImageRecord["confirmedAt"]["S"]+"'"
    else:
        confirmedAtValue = 'Default'

    queryString = queryString.format("'"+newImageRecord["userId"]["N"]+newImageRecord["uniqueAccountName"]["S"]+newImageRecord["txnId"]["S"]+"'",
    newImageRecord["userId"]["N"],
    "'"+newImageRecord["txnId"]["S"]+"'",
    "'"+newImageRecord["vendor"]["S"]+"'",
    "'"+newImageRecord["account"]["S"]+"'",
    "'"+newImageRecord["uniqueAccountName"]["S"]+"'",
    "'"+newImageRecord["uniqueAccountNameAndTxnId"]["S"]+"'",
    confirmedAtValue,
    "'"+newImageRecord["txnDate"]["S"]+"'",
    walletValue,
    "'"+newImageRecord["txnStatus"]["S"]+"'",
    "'"+newImageRecord["txnType"]["S"]+"'",
    "'"+newImageRecord["txnCategory"]["S"]+"'",
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
    return queryString;