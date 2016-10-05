if(window && window.console && window.console.log) {
	console.log("%c%s", "background:#ff0;color:red;font-size:20px;padding:10px", "WARNING!")
	console.log("You are exposed from Self-XSS attack. Do not use this console.")
}

Date.prototype.getMonthString = function() {
	return this.getMonth() < 9 ? "0" + (this.getMonth() + 1) : (this.getMonth() + 1).toString();
}
Date.prototype.getDateString = function() {
	return this.getDate() < 10 ? "0" + this.getDate() : this.getDate().toString();
}
Date.prototype.format = function(pattern) {
	var term;
	term = [
 		 this.getFullYear()
   		,this.getFullYear().toString().substring(2, 4)
   		,this.getMonthString()
   		,this.getDateString()
   		,(this.getHours() < 10) ? "0" + this.getHours() : this.getHours()
   		,(this.getHours() > 12) ? (this.getHours() - 12 < 10 ? "0" + (this.getHours() - 12) : this.getHours() - 12) : ((this.getHours() < 10) ? "0" + this.getHours() : this.getHours())
   		,(this.getMinutes() < 10) ? "0" + this.getMinutes() : this.getMinutes()
   		,(this.getSeconds() < 10) ? "0" + this.getSeconds() : this.getSeconds()
   		,(this.getMilliseconds() < 10) ? "00" + this.getMilliseconds() : (this.getMilliseconds() < 100 ? "0" + this.getMilliseconds() : this.getMilliseconds())
   	];
	   	
   	return (pattern || "yyyy-MM-dd")
   			.replace(/yyyy/g,	term[0])
   			.replace(/yy/g, 	term[1])
   			.replace(/MM/g,		term[2])
   			.replace(/M/g,		this.getMonth() + 1)
   			.replace(/dd/g,		term[3])
   			.replace(/d/g,		this.getDate())
   			.replace(/HH/g,		term[4])
   			.replace(/hh/g,		term[5])
   			.replace(/H/ig,		this.getHours())
   			.replace(/mm/g,		term[6])
   			.replace(/m/g,		this.getMinutes())
   			.replace(/ss/g,		term[7])
   			.replace(/s/g,		this.getSeconds());
}

String.prototype.format = {
	"tel": function(tel) {
	    if(!tel) return ""
	    tel = tel.replace(/[^0-9]+/g, "")

	    if(tel.length === 7) {
	    	return tel.replace(/([0-9]{3})([0-9]{4})/, "$1-$2")
	    }
	    else if(tel.length === 8) {
			return tel.replace(/([0-9]{4})([0-9]{4})/, "$1-$2")
	    }
	    else if(tel.length === 9) {
	    	return tel.replace(/([0-9]{2})([0-9]{3})([0-9]{4})/, "$1-$2-$3")
	    }
	    else if(tel.length === 10) {
			if(/^02/.test(tel))
	    		return tel.replace(/([0-9]{2})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
			else
	    		return tel.replace(/([0-9]{3})([0-9]{3})([0-9]{4})/, "$1-$2-$3")
	    }
	    else if(tel.length === 11) {
			return tel.replace(/([0-9]{3})([0-9]{4})([0-9]{4})/, "$1-$2-$3")
	    }
	    
	    return tel
	},
	"num": function(n) {
		if(!n) return n;

		if(typeof n === "number") {
			n = n + ""
		}
		else {
			n = n.replace(/[^0-9]+/g, "");
			if(!n) return n
		}

		return n.replace(/\B(?=(\d{3})+(?!\d))/g, ",")
	}
}
