<template>
  <div class="compiler">
    <h1>Compiler</h1>
    <div class="input-field">
      <label for="input">Input</label>
      <textarea id="input" v-model="input"></textarea>
    </div>
    <button @click="compile">Compile</button>
    <div class="output-field">
      <label for="output">Output</label>
      <textarea id="output" v-model="output"></textarea>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "CompilerComponent",
  data() {
    return {
      input: "",
      output: "",
    };
  },
  methods: {
    compile() {
      axios
        .post("http://localhost:8081/compile", {
          input: this.input,
        })
        .then((response) => {
          this.output = response.data;
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
};
</script>

<style scoped></style>
