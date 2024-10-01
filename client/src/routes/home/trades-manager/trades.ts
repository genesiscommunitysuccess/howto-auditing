import { User } from '@genesislcap/foundation-user';
import { customElement, GenesisElement } from '@genesislcap/web-core';
import { TradesStyles as styles } from './trades.styles';
import { TradesTemplate as template } from './trades.template';
import { EventEmitter } from '@genesislcap/foundation-events';
import { StoreEventDetailMap } from '../../../store';

@customElement({
  name: 'home-trades-manager',
  template,
  styles,
})
export class HomeTradesManager extends EventEmitter<StoreEventDetailMap>(GenesisElement) {
  @User user: User;

  tradeManagerGridOptions = {
    onRowClicked: (e) => {
      const payload = e.node.isSelected() ? e.data?.TRADE_ID : undefined;
      this.$emit('change-trade-id', payload);
    },
  };
}
