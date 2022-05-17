<script>
  import { StructuredList, StructuredListBody, StructuredListCell, StructuredListHead, StructuredListRow, StructuredListSkeleton } from "carbon-components-svelte";

  import { onMount } from "svelte";
  import { apiFetch } from "../api";


  export let populationDataList = []
  onMount(async () => {
    const res = await apiFetch("/unauth/population")

   let  populationData= await res.json()
   populationDataList = populationData.data
  })
</script>

{#if populationDataList.length === 0}

  <StructuredListSkeleton rows={3}/>
{:else}
  <StructuredList border>
    <StructuredListHead>
      <StructuredListRow head>
        <StructuredListCell head>Nation</StructuredListCell>
        <StructuredListCell head>Population</StructuredListCell>
        <StructuredListCell head>YEAR</StructuredListCell>
      </StructuredListRow>
    </StructuredListHead>
    <StructuredListBody>
      {#each populationDataList as data}
      <StructuredListRow label for="row-{data.Year}">
        <StructuredListCell>{data.Nation}</StructuredListCell>
        <StructuredListCell>{data.Population}</StructuredListCell>
        <StructuredListCell>{data.Year}</StructuredListCell>
      </StructuredListRow>
      {/each}
    </StructuredListBody>
  </StructuredList>
{/if}
