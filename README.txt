1 - A aplicação está implementada para trabalhar com o endereço de IP associado à rede
RICS_LAB. Se trabalhar com outra rede é necessário mudar o endereço de IP.

2 - A aplicação está implementada para trabalhar com o servidor da cloud IBM Bluemix.
É necessário criar outra conta na cloud IBM Bluemix para trabalhar com uma conta
diferente. O link do JSON irá mudar. É necessário alterar esse link quando se escolher
a opção Check Data da Main Activity. O link é alterado, criando uma database no servidor
e clicando na opção ver todos os JSONs. Fazer copy-paste do link e alterar a string na
função getJSONfromUrl. Na função JSONParser é necessário fazer copy-paste do link até à
última barra "/". O que estiver para a frente do link já não é necessário (corresponde ao
id do JSON que contém todas as informações).

3 - A aplicação, por default, contém uma conta já criada com o nome de utilizador "a",
password "a" e nome da piscina "a". Para se alterar isso, é necessário fazer debug à
aplicação e retirar do XML das caixas de texto da SignIn Activity os textos "a" e "a".