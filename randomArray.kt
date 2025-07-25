fun main(){
    var array = IntArray(50)
    var randomIndex = mutableListOf<Int>()

    for (i in 0..49){
        val number = (1..1000).random()
        array[i] = number
    }

    println("Random ${array.size} Numbers are: ")
    for (i in 0..49) {
        print("${array[i]} ")
    }

    var mutableListNumbers = array.toMutableList()

    for (i in 1..25){
        val randomDeleteIndex = (0..49).random()
        randomIndex.add(randomDeleteIndex)
        mutableListNumbers[randomDeleteIndex] = 0
    }

    println()
    println("The Index deleted are:")
    for (i in 0..randomIndex.size-1) {
        print("${randomIndex[i]} ")
    }

    var newArray = mutableListOf<Int>()
    var index = 0
    for (i in 0..49){
        if (mutableListNumbers[i] != 0){
            newArray.add(mutableListNumbers[i])
            index++
        }
    }

    println()
    println("After removing: ${newArray.size}")
    for (i in 0..newArray.size-1){
        print("${newArray[i]} ")
    }
}


/*

sample output: 

Random 50 Numbers are: 
935 181 629 996 903 399 639 174 640 1 267 215 481 770 483 682 186 252 535 287 836 830 873 338 798 538 935 572 412 161 909 299 302 279 447 96 38 408 3 461 207 751 268 899 858 149 13 441 355 991 
The Index deleted are:
15 27 20 31 40 28 4 24 35 47 0 32 41 5 48 12 15 16 5 14 35 30 38 34 43 
After removing: 28
181 629 996 639 174 640 1 267 215 770 252 535 287 830 873 338 538 935 161 279 38 408 461 268 858 149 13 991 

*/
