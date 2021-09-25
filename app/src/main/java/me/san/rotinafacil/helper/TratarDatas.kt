package me.san.rotinafacil.helper

class TratarDatas {

    companion object {
        fun dataParaFirebase(dataSemFormatacao: String): String {
            val dateArray = dataSemFormatacao.split("/")
            return "${dateArray[2]}/${dateArray[1]}/${dateArray[0]}"
        }
    }

}