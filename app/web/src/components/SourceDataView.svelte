<script>
  import { Column, Grid, Row } from "carbon-components-svelte";

  import Notifications from "./Notifications.svelte";
  import WagerList from "./PopulationList.svelte";
  import QrCode from "svelte-qrcode"
  let populationDataList = []



</script>

<Grid>
  <Row>
    <Column>
      <h2>Source</h2>

  <QrCode value="http://www.census.gov/programs-surveys/acs/" />
    </Column>
    <Column>
      <h2>US Population</h2>
      <WagerList bind:wagers={populationDataList}/>
    </Column>
  </Row>
</Grid>
<Notifications/>
