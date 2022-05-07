<script>
  import BetAdmin from "./BetAdmin.svelte";
  import BetWagerView from "./BetWagerView.svelte";
  import Notifications from "./Notifications.svelte";

  export let route = "home"

</script>

{#if route == "home"}
  <BetWagerView/>
{:else if route == "betAdmin"}
  <BetAdmin/>
{/if}

<Notifications/>
