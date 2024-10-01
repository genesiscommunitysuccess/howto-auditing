import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement, volatile } from '@genesislcap/web-core';
import { TradesStyles as styles } from './trades.styles';
import { AuditTradesTemplate as template } from './trades.template';
import { Store } from '../../../store';
import { DatasourceConfiguration } from '@genesislcap/foundation-entity-management';

@customElement({
  name: 'audit-trades-manager',
  template,
  styles,
})
export class AuditTradesManager extends GenesisElement {
  @Store store: Store;
  @User user: User;

  @volatile
  get tradesFilterCriteria(): DatasourceConfiguration {
    if (!this.store.tradeId) {
      return { criteria: null };
    }

    return { criteria: `TRADE_ID == '${this.store.tradeId}'` };
  }
}
