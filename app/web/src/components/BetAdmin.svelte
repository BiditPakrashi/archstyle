<script>
  import { Button, ButtonSet, Form, FormGroup, StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton, TextInput } from "carbon-components-svelte";
  import Add20 from "carbon-icons-svelte/lib/Add20";
  import { onMount } from "svelte";
  import { apiFetch } from "../api";
  import { notify } from "../store/notification";

  let bets = []
  onMount(async () => {
    const res = await apiFetch("/bet?complete=false")
    bets = await res.json()
  })

  let description = ""
  async function postNewBet() {
    let body = {
      description: description,
      complete: false,
    }
    const res = await apiFetch(
      `/bet`,
      {
        method: "POST",
        body: JSON.stringify(body),
      }
    )
    if (res.ok) {
      const b = await res.json()
      notify(`Created bet ${b.id}`, "success")
      bets = [...bets, b]
    } else {
      notify("Bet creation error", "error")
    }
  }

  async function postCompleteBet(betId, outcome) {
    let body = {
      outcome: outcome,
    }
    const res = await apiFetch(
      `/bet/${betId}/complete`,
      {
        method: "POST",
        body: JSON.stringify(body),
      }
    )
    if (res.ok) {
      notify("Successful payout", "success")
      for (let i = 0; i < bets.length; i++) {
        const b = bets[i];
        if (b.id == betId) {
          let bets2 = bets
          bets2.splice(i, 1)
          bets = bets2
        }
      }
    } else {
      notify("Payout error", "error")
    }
  }
</script>

<h3>New Bet</h3>
<Form on:submit={postNewBet}>
  <FormGroup>
    <TextInput labelText="Description" type="text" bind:value={description}></TextInput>
  </FormGroup>
  <Button type="submit" icon={Add20}>Create</Button>
</Form>

<h3>Payout Bet</h3>
{#if bets.length === 0}
  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>Id</StructuredListCell>
        <StructuredListCell head>Description</StructuredListCell>
        <StructuredListCell head>Payout</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each bets as bet}
      <StructuredListRow label for="row-{bet.id}">
        <StructuredListCell>{bet.id}</StructuredListCell>
        <StructuredListCell>{bet.description}</StructuredListCell>
        <StructuredListCell>
          <ButtonSet>
            <Button size="small" on:click={() => {postCompleteBet(bet.id, true)}}>For</Button>
            <Button size="small" on:click={() => {postCompleteBet(bet.id, false)}}>Against</Button>
          </ButtonSet>
        </StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}
