import { html, whenElse, repeat } from '@genesislcap/web-core';
import { getViewUpdateRightComponent } from '../../../utils';
import type { HomeTradesManager } from './trades';
import { createFormSchema } from './trades.create.form.schema';
import { updateFormSchema } from './trades.update.form.schema';
import {auditColumnDefs, columnDefs} from './trades.column.defs';
import {AuditTradesManager} from "./audit-trades";


export const TradesTemplate = html<HomeTradesManager>`
    ${whenElse(
        (x) => getViewUpdateRightComponent(x.user, ''),
        html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="ALL_TRADES"
                :gridOptions=${(x) => x.tradeManagerGridOptions}
                createEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADE_INSERT')}"
                :createFormUiSchema=${() => createFormSchema }
                updateEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADE_MODIFY')}"
                :updateFormUiSchema=${() => updateFormSchema}
                deleteEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADE_DELETE')}"
                :columns=${() => columnDefs }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
        html`
          <not-permitted-component></not-permitted-component>
        `,
    )}`;

export const AuditTradesTemplate = html<AuditTradesManager>`
    ${whenElse(
  (x) => getViewUpdateRightComponent(x.user, ''),
  html`
            <entity-management
                design-system-prefix="rapid"
                header-case-type="capitalCase"
                enable-row-flashing
                enable-cell-flashing
                resourceName="TRADE_AUDIT"
                :datasourceConfig=${(x) => ({...x.tradesFilterCriteria, pollingInterval: 5000 })}
                :updateFormUiSchema=${() => updateFormSchema}
                deleteEvent="${(x) => getViewUpdateRightComponent(x.user, '', 'EVENT_TRADE_DELETE')}"
                :columns=${() => columnDefs.concat(auditColumnDefs) }
                modal-position="centre"
                size-columns-to-fit
            ></entity-management>
        `,
  html`
          <not-permitted-component></not-permitted-component>
        `,
)}`;
