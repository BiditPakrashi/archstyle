import { writable } from 'svelte/store';

let _user = sessionStorage.getItem('storeUser');
export const store = writable(_user ? JSON.parse(_user) : null);
store.subscribe((val) => {
    if (val) sessionStorage.setItem('storeUser', JSON.stringify(val));
    else sessionStorage.removeItem('storeUser');
});
