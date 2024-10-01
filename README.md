# Auditing

If you want to be able to track the changes made to a table, you need an audit table. This is easily achieved in Genesis apps by setting your table as "auditable". Changes to the table will then be tracked, recording times and users affecting records when they are inserted, updated or deleted, to provide an accurate audit record of events.


## Examples

### Tables dictionary

To enable auditing on a table, specify the parameter `audit = details(id = X, sequence = Y)` on it within the tables dictionary [`howto-auditing-tables-dictionary.kts`](server/howto-auditing-app/src/main/genesis/cfg/howto-auditing-tables-dictionary.kts). Here you can see the audit table’s unique ID is set to 11_500 and records will have their generated id [sequence](https://docs.genesis.global/docs/develop/server-capabilities/data-model/#sequence) contain TA. This will create a table with name TRADE_AUDIT.

### GPAL Event Handlers

When using GPAL event handlers, audit records will be automatically created if the table is auditable. An example can be seen in [`howto-auditing-eventhandler.kts`](server/howto-auditing-app/src/main/genesis/scripts/howto-auditing-eventhandler.kts). 

In that example, the TRADE_INSERT event handler inserts records into our TRADE table. When `entityDb.insert(details)` is executed an audit record will be inserted into the TRADE_AUDIT table.

With the eventHandler being transactional also, specified by `transactional = true`, we can be assured that there will always be a corresponding audit record for each inserted record.

The TRADE_MODIFY and TRADE_DELETE events will also ensure audit records are created. When an original record is deleted from the main table (in this case TRADE) the related audit records will persist.

### Java Event Handlers

It is important to note that if you are using Java Event Handlers then the records of an auditable table will not be automatically audited. Instead, you must create an audited version of entityDb using the database Java API.

The [`TradeJavaEventHandler.java`](server/howto-auditing-app/src/main/java/global/genesis/trades/TradeJavaEventHandler.java) (line 35) example shows the creation of an audited database object. This object can then be used as normal with the audit records being created automatically for each database operation.

### Displaying Audit Records

Auditable database objects can be queried using request replies or dataservers. Although, it is worth noting that auditing can be a heavy operation with respect to the amount of records being generated (e.g. if an app performs lots of modifications, there will be a lot of audit records)
and so the preferred way to query audit records is through request replies so as not to have to load them into memory when they might not be queried too often. The usage of dataservers onto audit tables is typically avoided for this reason.

Example can be seen in [`howto-auditing-reqrep.kts`](server/howto-auditing-app/src/main/genesis/scripts/howto-auditing-reqrep.kts).

### UI

On the UI side, the audit records can be viewed by querying a previously set up request reply. A convenient way to display the audit records is to use two grids, side by side, with the main grid showing the main records and the second grid showing the audit records.

A pub\sub mechanism can be setup between the grids so that clicking a row on the main grid filters the criteria on the second grid to only show audit records on the selected row, filtering by the original record's ID for instance.

Example can be seen [`trades.template.ts`](client/src/routes/home/trades-manager/trades.template.ts), paying attention also to [`audit-trades.ts`](client/src/routes/home/trades-manager/audit-trades.ts), specifically to the filtering criteria setup.

## How to use auditing in your own application

1. Open your project's tables dictionary which will be found at this path in your project `server/{productName}-app/src/main/genesis/cfg/{productName}-tables-dictionary.kts`.
2. Find the tables you want to add auditing to.
3. Specify the `audit` parameter on the chosen tables along with the audit table's ID and the sequence to use for the records' generated ID.

For more details check the documentation [here](https://docs.genesis.global/docs/develop/server-capabilities/data-model/#audited-tables).

## Testing
The goal of this automation was to create a generic steps to be reused for any
CRUD operation with auditing, in this howto we use the example of a trade
object, however the steps can be reused for any type of object following the
same pattern.

in this example and like in all the How-tos we used the standard automation
tools : CucumberBDD Java, Rest Assured library for Api Testing and Playwright for
UI testing

in this example there is no UI testing as the CRUD processing and Audit are
backend operations.

in most of the steps payloads are prepared to insert, modify and delete objects
we verify that the return code is correct and we also get the data back (actual
execution results(`resources/2-actual`) and compare it to expected results (`resources/3-expected`),


This Gherkin step to authenticate to the app with ```EVENT_LOGIN_AUTH```

```Given User connect with username "admin" and password "genesis"```

This Gherkin step to insert object, in this case ```Trade``` with payload to object event ``EVENT_TRADE_INSERT``

```When User insert a "<object>" with the "<payload>" using the "<object_event>"```

This Gherkin step to find the object using the req rep with specified query param, the query param passed as ``"key" and "value" ``

```And User find the "<object>" with "<key>" as "<value>" and extract "<object_id>"```

This Gherkin step to verify the object successfully created in object table and comparing the expected json file with actual json file. Primary key and keys to ignore used while comparing two json files. 

```Then User verifies the "<object>" successfully created comparing to the "<expected_result>" with "<primary_key>" and "<keys_to_ignore>"```

This Gherkin step to verify the object successfully created in the object audit table and comparing the expected vs actual json files.

```And User verifies the "<object>" "<CRUD>" audit was created using the "<object_event>" and compares to "<expected_audit_result>" with "<primary_key>" and "<keys_to_ignore>"```

# Test results
BDD test results can be found [here](https://genesiscommunitysuccess.github.io/howto-auditing/test-results)
