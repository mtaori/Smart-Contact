 console.log("This is Script file")
 
 const search=() => {
    //console.log("seaching...");


    let query=$("#search-input");
    

    if(query==''){
        $(".search-result").hide();
    }else{
        console.log(query);
        //sending request to server
        let url=`http://localhost:8080//search/${query}`
        fetch(url).then((response) => {
            return response.json();
        })
        .then((data) =>{
            console.log(data);

            let text=`<div class='list-group'>`
            data.forEach((contact) => {
                text+=`<a href='#' class='list-group-item list-group-action'>${contact.name}</a>`
            });

            text+=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();
        });

        $(".search-result").show();
    }
};