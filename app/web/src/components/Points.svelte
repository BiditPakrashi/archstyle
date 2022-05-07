<script>
  import { store } from "../store/auth.js";
  import { apiFetch } from "../api.js";
  import { onMount } from "svelte";

  onMount(async () => {
    const res = await apiFetch("/user/me")
    let user = await res.json()
    $store = user
  })
</script>

<li>
  <!-- svelte-ignore a11y-missing-attribute -->
  <a class:bx--header__menu-item="{true}">
    <span class:bx--text-truncate--end="{true}">
      Points: {$store.amount}
    </span>
  </a>
</li>
