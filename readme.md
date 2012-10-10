## Middleware

#### PHASE 0
We are given a centralized Mock Stock application that works via rich client desktop app.

#### PHASE 1
We divided into 2 parts, namely market(server) generates stock prices and trader(client) buys or sells stocks.
Communication between market and trader is handled by JMS. Price update is done via a topic and 'Buy' and 'Sell' transactions
are done via queue. Moreover, we added a web tier for ease of access.

#### PHASE 2
We made use of beans and made the application persistent. Portfolios of the traders are kept between sessions.

#### PHASE 3
Mobile client(iPhone) is added and it utilizes zero conf protocol Bonjour, it can easily find the service in its local. 
Moreover, we have added some servlets(wrapper on web client) whom mobile client can talk and serialization in between is done via JSON.  