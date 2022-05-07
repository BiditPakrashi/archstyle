<script>
  import { Form, FormGroup, InlineLoading, Modal, NumberInput, StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton, Toggle } from "carbon-components-svelte";
  import { createEventDispatcher, onMount } from "svelte";
  import { apiFetch } from "../api";
  import { betStore } from "../store/bet";
  import { notify } from "../store/notification";

  const dispatch = createEventDispatcher()
  const eventWagerPlaced = "wagerPlaced"

  let bets = []
  onMount(async () => {
    const res = await apiFetch("/bet?complete=false")
    bets = await res.json()
    betStore.set(bets)
  })

  let modalOpen = false
  let currentBet = {description:""}
  function openWagerModal(bet) {
    modalOpen = true
    currentBet = bet
  }
  
  let amount = 1
  let outcome = true
  async function postWager(betId, wagerAmount, wagerOutcome) {
    let newWager = {
      amount: wagerAmount,
      outcome: wagerOutcome,
      betId: betId,
      userId: betId, // api needs any uuid here
    }
    const res = await apiFetch(
      `/bet/${betId}/wager`,
      {
        method: "POST",
        body: JSON.stringify(newWager)
      }
    )
    if (res.ok) {
      notify("Successful wager", "success")
      dispatch(eventWagerPlaced, newWager)
    } else {
      notify("Wager error", "error")
    }
    postWagerPromise = null
  }
  let postWagerPromise = null
  function handleModalSubmit() {
    postWagerPromise = postWager(currentBet.id, amount, outcome)
    modalOpen = false
  }
</script>

{#if bets.length === 0}
  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList selection border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>Description</StructuredListCell>
        <StructuredListCell head>For : Against</StructuredListCell>
        <StructuredListCell head>{""}</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each bets as bet}
      <StructuredListRow label for="row-{bet.id}" on:click={() => openWagerModal(bet)}>
        <StructuredListCell>{bet.description}</StructuredListCell>
        <StructuredListCell>{bet.odds_for?.toFixed(2) || "-"}:{bet.odds_against?.toFixed(2) || "-"}</StructuredListCell>
        <StructuredListCell>
          {#if currentBet == bet && postWagerPromise != null}
            {#await postWagerPromise}
            <InlineLoading status="active" description="loading..." />
            {:catch error}
            <InlineLoading status="error" description={error} />
            {/await}
          {/if}
        </StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}

<Modal
  bind:open={modalOpen}
  modalHeading="Place wager"
  primaryButtonText="Confirm"
  secondaryButtonText="Cancel"
  on:click:button--secondary={() => (modalOpen = false)}
  on:submit={handleModalSubmit}
>
  <p>{currentBet.description}</p>
  <Form>
    <FormGroup>
      <NumberInput min={1} invalidText="Must place a non-0 wager" label="Amount" bind:value={amount}/>
    </FormGroup>
    <FormGroup>
      <Toggle labelText="Outcome" labelA="Against" labelB="For" bind:toggled={outcome}/>
    </FormGroup>
  </Form>
</Modal>
