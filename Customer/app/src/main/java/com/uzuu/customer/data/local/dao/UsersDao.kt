package com.uzuu.customer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.uzuu.customer.data.local.entity.UsersEntity
import com.uzuu.customer.data.local.entity.EventEntity
import com.uzuu.customer.data.local.entity.TicketEntity
import com.uzuu.customer.data.local.entity.CartEntity
import com.uzuu.customer.data.local.entity.CartItemEntity
import com.uzuu.customer.data.local.entity.OrderEntity
import com.uzuu.customer.data.local.entity.MyTicketEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface UsersDao {
    @Query("select * from users order by id asc")
    fun observeUser(): Flow<List<UsersEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createUser(user: UsersEntity) : Long

    @Update
    suspend fun updateUser(user: UsersEntity) : Int

    @Query("delete from users where id = :id")
    suspend fun deleteUserById(id: Int): Int

    //
    @Query("delete from users where username = :username")
    suspend fun deleteUserByUsername(username: String): Int

    @Query("select * from users where username = :username limit 1")
    suspend fun getUserByUsername(username: String) : UsersEntity

    //
    @Query("select exists(select 1 from users where id = :id)")
    suspend fun checkUserExists(id: Int): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE username = :username)")
    suspend fun isUserExist(username: String): Boolean
}

@Dao
interface EventDao {
    @Query("SELECT * FROM events ORDER BY id ASC")
    fun observeAllEvents(): Flow<List<EventEntity>>

    @Query("SELECT * FROM events WHERE id = :id")
    suspend fun getEventById(id: Long): EventEntity?

    @Query("SELECT * FROM events WHERE lastUpdated > :timestamp")
    suspend fun getEventsUpdatedAfter(timestamp: Long): List<EventEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Query("DELETE FROM events")
    suspend fun deleteAllEvents()

    @Query("SELECT COUNT(*) FROM events")
    suspend fun getEventCount(): Int
}

@Dao
interface TicketDao {
    @Query("SELECT * FROM tickets WHERE eventId = :eventId ORDER BY id ASC")
    fun observeTicketsByEvent(eventId: Long): Flow<List<TicketEntity>>

    @Query("SELECT * FROM tickets WHERE id = :id")
    suspend fun getTicketById(id: Long): TicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<TicketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: TicketEntity)

    @Query("DELETE FROM tickets WHERE eventId = :eventId")
    suspend fun deleteTicketsByEvent(eventId: Long)

    @Query("DELETE FROM tickets")
    suspend fun deleteAllTickets()
}

@Dao
interface CartDao {
    @Query("SELECT * FROM cart LIMIT 1")
    fun observeCart(): Flow<CartEntity?>

    @Query("SELECT * FROM cart_items ORDER BY id ASC")
    fun observeCartItems(): Flow<List<CartItemEntity>>

    @Query("SELECT * FROM cart LIMIT 1")
    suspend fun getCart(): CartEntity?

    @Query("SELECT * FROM cart_items ORDER BY id ASC")
    suspend fun getCartItems(): List<CartItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCart(cart: CartEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItems(items: List<CartItemEntity>)

    @Query("DELETE FROM cart")
    suspend fun deleteCart()

    @Query("DELETE FROM cart_items")
    suspend fun deleteCartItems()

    @Query("DELETE FROM cart_items WHERE id = :itemId")
    suspend fun deleteCartItem(itemId: Long)

    @Query("UPDATE cart_items SET quantity = :quantity, subtotal = :subtotal, lastUpdated = :lastUpdated WHERE id = :itemId")
    suspend fun updateCartItem(itemId: Long, quantity: Int, subtotal: Double, lastUpdated: Long)
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders ORDER BY lastUpdated DESC")
    fun observeOrders(): Flow<List<OrderEntity>>

    @Query("SELECT * FROM orders WHERE id = :id")
    suspend fun getOrderById(id: Long): OrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()
}

@Dao
interface MyTicketDao {
    @Query("SELECT * FROM my_tickets ORDER BY id ASC")
    fun observeMyTickets(): Flow<List<MyTicketEntity>>

    @Query("SELECT * FROM my_tickets WHERE id = :id")
    suspend fun getTicketById(id: Long): MyTicketEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTickets(tickets: List<MyTicketEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: MyTicketEntity)

    @Query("DELETE FROM my_tickets")
    suspend fun deleteAllTickets()
}