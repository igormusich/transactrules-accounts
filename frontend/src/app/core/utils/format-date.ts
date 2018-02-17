export function toIsoString(data:Date){
    if(data==null){
        return null;
    }

    return data.getFullYear()+"-"+ pad(data.getMonth()+1,2)+ "-" + pad(data.getDate(),2);
}

export function toDate(isoDate:string):Date{

    if(isoDate==null){
        return null;
    }

    var parts:string[] =isoDate.split('-');
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    var mydate = new Date( Number(parts[0]), Number(parts[1]) - 1, Number(parts[2]));

    return mydate;
}

function pad(num, size) {
    var s = "000000000" + num;
    return s.substr(s.length-size);
}

