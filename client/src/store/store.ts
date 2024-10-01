import { CustomEventMap } from '@genesislcap/foundation-events';
import { getApp } from '@genesislcap/foundation-shell/app';
import {
  AbstractStoreRoot,
  registerStore,
  StoreRoot,
  StoreRootEventDetailMap,
} from '@genesislcap/foundation-store';
import { observable } from '@genesislcap/web-core';

export interface Store extends StoreRoot {
  readonly tradeId: string;
}

export type StoreEventDetailMap = StoreRootEventDetailMap & {
  'change-trade-id': string;
};

declare global {
  interface HTMLElementEventMap extends CustomEventMap<StoreEventDetailMap> {}
}

class DefaultStore extends AbstractStoreRoot<Store, StoreEventDetailMap> implements Store {
  @observable tradeId: string = '';

  constructor() {
    super();

    /**
     * Register the store root
     */
    getApp().registerStoreRoot(this);

    this.createListener<string>('change-trade-id', (tradeId) => {
      this.commit.tradeId = tradeId;
    });
  }
}

export const Store = registerStore(DefaultStore, 'Store');
