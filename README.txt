1 - A aplica��o est� implementada para trabalhar com o endere�o de IP associado � rede
RICS_LAB. Se trabalhar com outra rede � necess�rio mudar o endere�o de IP.

2 - A aplica��o est� implementada para trabalhar com o servidor da cloud IBM Bluemix.
� necess�rio criar outra conta na cloud IBM Bluemix para trabalhar com uma conta
diferente. O link do JSON ir� mudar. � necess�rio alterar esse link quando se escolher
a op��o Check Data da Main Activity. O link � alterado, criando uma database no servidor
e clicando na op��o ver todos os JSONs. Fazer copy-paste do link e alterar a string na
fun��o getJSONfromUrl. Na fun��o JSONParser � necess�rio fazer copy-paste do link at� �
�ltima barra "/". O que estiver para a frente do link j� n�o � necess�rio (corresponde ao
id do JSON que cont�m todas as informa��es).

3 - A aplica��o, por default, cont�m uma conta j� criada com o nome de utilizador "a",
password "a" e nome da piscina "a". Para se alterar isso, � necess�rio fazer debug �
aplica��o e retirar do XML das caixas de texto da SignIn Activity os textos "a" e "a".