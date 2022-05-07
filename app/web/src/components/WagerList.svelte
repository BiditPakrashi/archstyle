<script>
  import { StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton } from "carbon-components-svelte";

  import { onMount } from "svelte";
  import { apiFetch } from "../api";
  import { betStore } from "../store/bet";

  export let wagers = []
  onMount(async () => {
    const res = await apiFetch("/wager/user?complete=false")
    // wait for the bets query in the sibling component
    await new Promise(resolve => {
      function checkBets() {
        if ($betStore.length > 0) {
          resolve()
        } else {
          window.setTimeout(checkBets, 1000)
        }
      }
      checkBets()
    })
    // then use the updated wager objects
    wagers = await res.json()
    wagers.map((w) => {
      $betStore.forEach(b => {
        if (b.id == w.betId) {
          w.betDescription = b.description
        }
      })
    })
    // TODO: event bus or something
  })
</script>

{#if wagers.length === 0}
  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>Bet</StructuredListCell>
        <StructuredListCell head>Outcome</StructuredListCell>
        <StructuredListCell head>Amount</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each wagers as wager}
      <StructuredListRow label for="row-{wager.id}">
        <StructuredListCell>{wager.betDescription || wager.betId}</StructuredListCell>
        <StructuredListCell>{wager.outcome ? "For" : "Against"}</StructuredListCell>
        <StructuredListCell>{wager.amount}</StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}
