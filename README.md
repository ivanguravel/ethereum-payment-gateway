# ethereum-payment-gateway

### 1) Starts as a simple spring boot app.
Creates an empty db. Regarding to the account access app saved public and private keys inside db. You can impoove that with helps some standart encryption. 
Swagger: `http://localhost:8091/swagger-ui.html`
H2 DB: `http://localhost:8091/h2`
user can  point app to the address of ethereum node inside: `application.yml`. Currently this is `http://127.0.0.1:8501` (for geth client)
port is related to the standart option `—rpcport "8501"`

### 2) How to test: 
One way  - rinkeby test network. 

instructions: https://gist.github.com/cryptogoth/10a98e8078cfd69f7ca892ddbdcf26bc

for getting money to the rinkeby wallet you can use: https://faucet.rinkeby.io/ 


Money was transfered within 3 hours by net to my wallet because a lot of queries.

### 3) Production ready case:  

`gateway -> geth node(on your server) -> main network.`

Example of settings:

`./geth.exe —networkid=0 —datadir=rinkeby —cache=1024 —syncmode=light —rpc —rpcaddr "127.0.0.1" —rpcport "8501" —rpcapi "db,eth,net,web3"`

`—networkid=0`  - main ethereum net id,
`—datadir=rinkeby` - local folder with network data,
`—cache=1024`  - cache,
`—syncmode=light`  -  download only transactions meta,
`—rpc —rpcaddr "127.0.0.1" —rpcport "8501" —rpcapi "db,eth,net,web3"` - client settings 
