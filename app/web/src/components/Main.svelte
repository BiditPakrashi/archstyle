<script>
  import BetAdmin from "./Admin.svelte";
  import BetWagerView from "./SourceDataView.svelte";
  import Notifications from "./Notifications.svelte";

  export let route = "home"

</script>

{#if route == "home"}
  <BetWagerView/>
{:else if route == "betAdmin"}
  <BetAdmin/>
{/if}

<Notifications/>
