<template>
  <div class="compiler">
    <h1>Compiler</h1>
    <textarea class="input" v-model="input" ></textarea>
    <button @click="compile()">Compile</button>
    <textarea class="output" v-model="output" ></textarea>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "CompilerComponent",
  data() {
    return {
      input: "Enter code here!",
      output: "",
    };
  },
  methods: {
    compile() {
      axios
        .post("http://localhost:8081/compile", {
          code: this.input,
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

<style scoped>
.compiler {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.input {
  width: 100%;
  height: 10em;
  resize: none;
}

.output {
  width: 100%;
  height: 10em;
  resize: none;
}

button {
  width: 100%;
  height: 5em;
  margin: 1em;
}

textarea {
  font-family: monospace;
  font-size: 1.2em;
}
</style>
