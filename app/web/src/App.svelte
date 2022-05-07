<script>
  import {
    Header,
    HeaderNav,
    HeaderUtilities,
    SkipToContent,
    Content,
    HeaderGlobalAction,
    HeaderNavItem,
  } from "carbon-components-svelte";
  import Logout20 from "carbon-icons-svelte/lib/Logout20";
  import Login from "./components/Login.svelte";
  import Theme from "./components/Theme.svelte";
  import { store } from "./store/auth.js";
  import Main from "./components/Main.svelte";
  import Points from "./components/Points.svelte";

  let isSideNavOpen = false;

  function handleLogout() {
    $store = null
    location.reload()
  }

  let route = "home"
</script>

<Theme theme="g90">
  
  <Header company="" platformName="Trashbet" bind:isSideNavOpen>
    <div slot="skip-to-content">
      <SkipToContent />
    </div>
    
    {#if $store != null}
      <HeaderNav>
        <Points/>
        {#if $store.admin}
          <HeaderNavItem text="Home" on:click={() => {route = "home"}}/>
          <HeaderNavItem text="Bets admin" on:click={() => {route = "betAdmin"}}/>
        {/if}
      </HeaderNav>
      
      <HeaderUtilities>
        <HeaderGlobalAction aria-label="Logout" icon={Logout20} on:click={handleLogout}/>
      </HeaderUtilities>
    {/if}
  </Header>
    
  <Content>
    {#if $store != null}
      <Main bind:route/>
    {:else}
      <Login/>
    {/if}
  </Content>
</Theme>
