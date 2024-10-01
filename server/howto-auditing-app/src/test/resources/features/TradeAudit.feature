Feature: Trade creation modification deletion and Audit

  @API
  Scenario Outline: create <object> and verify the creation.
    Given User connect with username "admin" and password "genesis"
    When User insert a "<object>" with the "<payload>" using the "<object_event>"
    And User find the "<object>" with "<key>" as "<value>" and extract "<object_id>"
    Then User verifies the "<object>" successfully created comparing to the "<expected_result>" with "<primary_key>" and "<keys_to_ignore>"
    And User verifies the "<object>" "<CRUD>" audit was created using the "<object_event>" and compares to "<expected_audit_result>" with "<primary_key>" and "<keys_to_ignore>"
    Examples:
      | object | key           | value   | object_id | payload                        | object_event       | expected_result                | CRUD      | expected_audit_result                 | primary_key      | keys_to_ignore                                                   |
      | trade  | CUSTOMER_NAME | Genesis | TRADE_ID  | /TradeAudit/P_tradeInsert.json | EVENT_TRADE_INSERT | /TradeAudit/E_tradeInsert.json | insertion | /TradeAudit/E_tradeAuditInserted.json | AUDIT_EVENT_TYPE | TRADE_ID,TRADE_AUDIT_ID,AUDIT_EVENT_DATETIME,RECORD_ID,TIMESTAMP |

  @API
  Scenario Outline: Verify <object> Audit after <object> modified
    Given User connect with username "admin" and password "genesis"
    And User find the "<object>" with "<key>" as "<value>" and extract "<object_id>"
    Then User modifies the "<object>" with the "<payload>" and "<object_event>"
    And User verifies the "<object>" successfully modified comparing to the "<expected_result>" with "<primary_key>" and "<keys_to_ignore>"
    And User verifies the "<object>" "<CRUD>" audit was created using the "<object_event>" and compares to "<expected_audit_result>" with "<primary_key>" and "<keys_to_ignore>"
    Examples:
      | object | key           | value   | object_id | payload                        | object_event       | expected_result                  | CRUD         | expected_audit_result                 | primary_key      | keys_to_ignore                                                   |
      | trade  | CUSTOMER_NAME | Genesis | TRADE_ID  | /TradeAudit/P_tradeModify.json | EVENT_TRADE_MODIFY | /TradeAudit/E_tradeModified.json | modification | /TradeAudit/E_tradeAuditModified.json | AUDIT_EVENT_TYPE | TRADE_ID,TRADE_AUDIT_ID,AUDIT_EVENT_DATETIME,RECORD_ID,TIMESTAMP |

  @API
  Scenario Outline: delete a <object>
    Given User connect with username "admin" and password "genesis"
    And User find the "<object>" with "<key>" as "<value>" and extract "<object_id>"
    Then User deletes the "<object>" with the "<payload>" using "<object_event>"
    And User verifies the "<object>" successfully deleted and comparing to the "<expected_result>" with "<primary_key>" and "<keys_to_ignore>"
    And User verifies the "<object>" "<CRUD>" audit was created using the "<object_event>" and compares to "<expected_audit_result>" with "<primary_key>" and "<keys_to_ignore>"
    Examples:
      | object | key           | value   | object_id | payload                        | object_event       | expected_result                 | CRUD     | expected_audit_result                | primary_key      | keys_to_ignore                                                   |
      | trade  | CUSTOMER_NAME | Genesis | TRADE_ID  | /TradeAudit/P_tradeDelete.json | EVENT_TRADE_DELETE | /TradeAudit/E_tradeDeleted.json | deletion | /TradeAudit/E_tradeAuditDeleted.json | AUDIT_EVENT_TYPE | TRADE_ID,TRADE_AUDIT_ID,AUDIT_EVENT_DATETIME,RECORD_ID,TIMESTAMP |