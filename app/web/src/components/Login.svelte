<script>
  import { Button, Form, FormGroup, InlineLoading, TextInput, Toggle } from "carbon-components-svelte";
  import { store } from "../store/auth.js";
  import { apiFetch } from "../api.js";
  let username = ""
  let password = ""
  let loginPromise = null
  let signup = false
  $: action = signup ? "Signup" : "Login"

  async function login() {
    let path = "/login"
    if (signup) {
      path = "/register"
    }
    const res = await apiFetch(path, {
      method: "POST",
      body: JSON.stringify({
        username: username,
        password: password,
      }),
    })
    if (res.ok) {
      $store = {
        "name": username,
        "amount": 0,
        "id": "",
      }
      return
    } else {
      throw new Error("bad login")
    }
  }

  function handleSubmit() {
    loginPromise = login()
  }
</script>
<div>
  <h2>{action}</h2>
  <Form on:submit="{handleSubmit}">
    <FormGroup>
      <TextInput labelText="Username" type="text" bind:value={username} />
      <TextInput labelText="Password" type="password" bind:value={password} />
    </FormGroup>
    <FormGroup>
      <Toggle labelText="Signup" bind:toggled={signup}/>
    </FormGroup>
    <Button type="submit">{action}</Button>
    {#await loginPromise}
      <InlineLoading status="active" description="loading..." />
    {:catch error}
      <InlineLoading status="error" description={error} />
    {/await}
  </Form>
</div>
