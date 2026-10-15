const express = require('express');
const cors = require('cors'); // 1. Importe o cors
const app = express();

app.use(cors()); // 2. Ative o cors para todas as rotas (coloque isso ANTES das suas rotas)
app.use(express.json());

async function login() {
    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let pwd = document.getElementById("pwd").value;

    //criando objeto com valor do login
    const login = {name, email,pwd};

    //tenta realizar o login 

try {
    const response = await fetch("http://127.0.0.1:8080/api/login", {
        method:'POST',
        headers: {
            "Content-Type":"application/json"
        },
        body: JSON.stringify(login)
    });
    const dados = await response.json();
    console.log(dados);
}  catch(error) {
    console.error("Erro na Requisição", error);
}
    







}
