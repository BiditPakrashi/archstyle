<script>
  import { ToastNotification } from "carbon-components-svelte";
  import { notificationStore } from "../store/notification";
</script>

<style>
  .notification-position {
    position: fixed;
    width: 400px;
    top: 0;
    right: 0;
  }
</style>

<div class="notification-position">
  {#each $notificationStore as n}
    <ToastNotification
      kind="{n.level}"
      title=""
      caption=""
      subtitle="{n.content}"
      timeout=7000
    />
  {/each}
</div>
