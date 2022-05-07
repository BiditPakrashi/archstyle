import { writable } from 'svelte/store';

export const notificationStore = writable([]);

export function notify(content, kind) {
    notificationStore.update((ns) => [
        ...ns,
        { content: content, level: kind }
    ]);
}
