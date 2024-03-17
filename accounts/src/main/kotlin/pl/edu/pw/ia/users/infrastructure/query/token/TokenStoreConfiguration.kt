package pl.edu.pw.ia.users.infrastructure.query.token

import com.mongodb.client.MongoClient
import org.axonframework.common.transaction.TransactionManager
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.extensions.mongo.DefaultMongoTemplate
import org.axonframework.extensions.mongo.eventsourcing.tokenstore.MongoTokenStore
import org.axonframework.serialization.Serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TokenStoreConfiguration {

	@Bean
	fun tokenStore(
		client: MongoClient,
		serializer: Serializer,
		transactionManager: TransactionManager
	): TokenStore =
		MongoTokenStore.builder()
			.mongoTemplate(
				DefaultMongoTemplate
					.builder()
					.mongoDatabase(client, "axon-accounts-query")
					.build()
			)
			.serializer(serializer)
			.transactionManager(transactionManager)
			.build()
}
