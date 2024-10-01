import { ColDef } from '@ag-grid-community/core';
import { getNumberFormatter, getDateFormatter } from '@genesislcap/foundation-utils';

export const columnDefs: ColDef[] = [
  {
    field: "TRADE_ID",
  },
  {
    field: "CUSTOMER_ID",
  },
  {
    field: "CUSTOMER_NAME",
  },
  {
    field: "COUNTRY_NAME",
  },
  {
    field: "NOTIONAL",
    valueFormatter: getNumberFormatter("0,0.00", null),
  },
  {
    field: "RATE",
    valueFormatter: getNumberFormatter("0,0.00", null),
  },
  {
    field: "SETTLEMENT_DATE",
  },
  {
    field: "SIDE",
  },
  {
    field: "SOURCE_CURRENCY",
  },
  {
    field: "TARGET_CURRENCY",
  },
  {
    field: "VERSION",
    valueFormatter: getNumberFormatter("0,0", null),
  }
]

export const auditColumnDefs: ColDef[] = [
  {
    field: "TRADE_AUDIT_ID",
  },
  {
    field: "AUDIT_EVENT_TYPE",
  },
  {
    field: "AUDIT_EVENT_DATETIME",
  },
  {
    field: "AUDIT_EVENT_TEXT",
  },
  {
    field: "AUDIT_EVENT_USER",
  }
]
