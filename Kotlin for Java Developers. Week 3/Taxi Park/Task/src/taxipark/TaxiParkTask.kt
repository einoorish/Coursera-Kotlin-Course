package taxipark

import kotlin.math.min
import kotlin.math.roundToInt

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = run {
//    val realDrivers: List<Driver> = this.trips.map { it.driver }
//    this.allDrivers.partition {d -> realDrivers.contains(d)}.second.toSet()

    allDrivers.minus(trips.map { it.driver }.toSet())
}



/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> = run {
//    val allPassengersInTrips = this.trips.flatMap { trip ->  trip.passengers}
//    val passengerAndNumOfTrips = this.allPassengers.associateWith { allPassengersInTrips.count { p -> p == it } }
//    passengerAndNumOfTrips.filter { (_, count) -> count >= minTrips }.map { it.key }.toSet()

    trips.flatMap(Trip::passengers)
        .groupBy{passenger -> passenger}
        .filterValues { group -> group.size >= minTrips }
        .map { it.key }.toSet()

    // another solution

    allPassengers.filter { p -> trips.count { p in it.passengers } >= minTrips }.toSet()
}


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> = run {
    val driverTrips = this.trips.filter { it.driver == driver }
    val passengersInTrips = driverTrips.flatMap { it.passengers }
    val regularPassengers = passengersInTrips.groupBy { it }.filter { it.value.count() > 1 }

    passengersInTrips.filter { regularPassengers.keys.contains(it) }.toSet()
}


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> = run {
//    val passengersWithDiscounts = this.trips.filter { trip ->
//        if(trip.discount!=null) trip.discount > 0.0
//        else false
//    }.map { it.passengers }
//
//    val passengersWithoutDiscounts = this.trips.filter { trip ->
//        if(trip.discount!=null) trip.discount == 0.0
//        else true
//    }.map { it.passengers }
//
//
//    val countDiscountedTrips: (passenger: Passenger) -> Int = { passanger ->
//        passengersWithDiscounts.count { it.contains(passanger) }
//    }
//
//    val countFullPriceTrips: (passenger: Passenger) -> Int = { passanger ->
//        passengersWithoutDiscounts.count { it.contains(passanger) }
//    }
//
//    this.allPassengers.filter { countDiscountedTrips(it) > countFullPriceTrips(it) }.toSet()

    val (tripsWithDiscount, tripsWithoutDiscount) = trips.partition { it.discount != null }

    allPassengers.filter { passenger ->
        tripsWithDiscount.count{ passenger in it.passengers } >
                tripsWithoutDiscount.count{ passenger in it.passengers }
    }.toSet()

    // another solution

    allPassengers.filter { passenger ->
        val withDiscount = trips.count{ t -> passenger in t.passengers && t.discount != null}
        val withoutDiscount = trips.count{ t -> passenger in t.passengers && t.discount == null}
        withDiscount > withoutDiscount
    }.toSet()

}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    return this.trips.groupBy {
        val periodStart: Int = it.duration / 10 * 10
        periodStart..periodStart+9
    }.maxBy { it.value.count() }?.key
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean  = run {
    if (trips.isEmpty()) return false

    val getDriverTrips: (driver: Driver) -> List<Trip> = {this.trips.filter { trip -> trip.driver == it }}

    val driverAndIncome = this.allDrivers
        .associate {getDriverTrips(it).map { t -> t.cost }.sum() to it}
        .toSortedMap(compareByDescending { it })

    val _20PercentOfDrivers = (this.allDrivers.count() * 0.2).toInt()
    val _80PercentOfIncome = driverAndIncome.map { it.key }.sum() * 0.8

    driverAndIncome.map { it.key }.take(_20PercentOfDrivers).sum()>= _80PercentOfIncome
}