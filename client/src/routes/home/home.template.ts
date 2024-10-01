import { isDev } from '@genesislcap/foundation-utils';
import { html } from '@genesislcap/web-core';
import type { Home } from './home';
import { HomeTradesManager } from './trades-manager';
import {AuditTradesManager} from "./trades-manager/audit-trades";

HomeTradesManager;
AuditTradesManager;

export const HomeTemplate = html<Home>`
  <rapid-layout auto-save-key="${() => (isDev() ? null : 'HOME_1723650011716')}">
     <rapid-layout-region>
         <rapid-layout-item title="Trades">
             <home-trades-manager></home-trades-manager>
         </rapid-layout-item>
       <rapid-layout-item title="Audit Trades">
         <audit-trades-manager></audit-trades-manager>
       </rapid-layout-item>
     </rapid-layout-region>
  </rapid-layout>
`;
