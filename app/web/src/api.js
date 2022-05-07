import { apiEndpoint } from './environment.js';

/**
 * Hit the API with a query
 * defaults to GET with application/json and auth
 * @param {string} query url query string
 * @param {Object} options options
 * @param {number} timeout
 */
export async function apiFetch(query, options = {}, timeout = 10000) {
    var defaults = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    };
    const url = apiEndpoint + query;
    return Promise.race([
        fetch(url, { ...defaults, ...options }),
        new Promise((_, reject) =>
            setTimeout(() => reject(new Error('timeout')), timeout)
        )
    ]);
}
