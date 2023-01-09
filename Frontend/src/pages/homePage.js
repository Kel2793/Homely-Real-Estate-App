import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ListingClient from "../api/listingClient";

/**
 * Logic needed for the view playlist page of the website.
 */
class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['onSearch', 'onGetListings', 'onCreate', 'renderHomeListing', 'renderHomeSearch'], this);
        this.dataStore = new DataStore();
    }

    /**
     * Once the page has loaded, set up the event handlers and fetch the listings.
     */
    async mount() {
        document.getElementById('create-form').addEventListener('submit', this.onCreate);
        document.getElementById('search-homes-form').addEventListener('submit', this.onSearch);
        document.getElementById('get-all-open-listings-form').addEventListener('submit', this.onGetListings);
        this.client = new ListingClient();

        this.dataStore.addChangeListener(this.renderHomeSearch);
        this.dataStore.addChangeListener(this.renderHomeListing);
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderHomeListing() {
        let listingResultArea = document.getElementById("all-listings-info");
        let allListings = this.dataStore.get("listings");

        listingResultArea.innerHTML = `
                <div class="row">
                    <table class="table-bordered">
                        <thead>
                            <tr>
                            
                                <th> Listing Number</th>
                                <th> Address</th>
                                <th> Square Footage</th>
                                <th> Price</th>
                                <th> Number of Bedrooms</th>
                                <th> Number of Bathrooms</th>
                                <th> Listing Status</th>
                                <th> Lot Size</th>
                            </tr>
                        </thead>
                        <tbody id="listAllHomes">
                        
            `;
        if (allListings && allListings.length !== 0) {
            const tableBody = document.getElementById("listAllHomes");
            for (let allListing of allListings) {
                let row = tableBody.insertRow();
                let listingNumber = row.insertCell(0);
                listingNumber.innerHTML = allListing.listingNumber;
                let address = row.insertCell(1);
                address.innerHTML = allListing.address;
                let squareFootage = row.insertCell(2);
                squareFootage.innerHTML = allListing.squareFootage;
                let price = row.insertCell(3);
                price.innerHTML = allListing.price;
                let numBedrooms = row.insertCell(4);
                numBedrooms.innerHTML = allListing.numBedrooms;
                let numBathrooms = row.insertCell(5);
                numBathrooms.innerHTML = allListing.numBathrooms;
                let listingStatus = row.insertCell(6);
                listingStatus.innerHTML = allListing.listingStatus;
                let lotSize = row.insertCell(7);
                lotSize.innerHTML = allListing.lotSize;

            }
        }
    }


    async renderHomeSearch() {
        let resultArea = document.getElementById("result-info");
        let searchResults = this.dataStore.get("searchedHomes");

            resultArea.innerHTML = `
                <div class="row">
                    <table class="table-bordered">
                        <thead>
                            <tr>
                            
                                <th> Listing Number</th>
                                <th> Address</th>
                                <th> Square Footage</th>
                                <th> Price</th>
                                <th> Number of Bedrooms</th>
                                <th> Number of Bathrooms</th>
                                <th> Listing Status</th>
                                <th> Lot Size</th>
                            </tr>
                        </thead>
                        <tbody id="homeSearch">
                        
            `;
        if (searchResults && searchResults.length !== 0) {
            const tableBody = document.getElementById("homeSearch");
            for (let searchResult of searchResults) {
                let row = tableBody.insertRow();
                let listingNumber = row.insertCell(0);
                listingNumber.innerHTML = searchResult.listingNumber;
                let address = row.insertCell(1);
                address.innerHTML = searchResult.address;
                let squareFootage = row.insertCell(2);
                squareFootage.innerHTML = searchResult.squareFootage;
                let price = row.insertCell(3);
                price.innerHTML = searchResult.price;
                let numBedrooms = row.insertCell(4);
                numBedrooms.innerHTML = searchResult.numBedrooms;
                let numBathrooms = row.insertCell(5);
                numBathrooms.innerHTML = searchResult.numBathrooms;
                let listingStatus = row.insertCell(6);
                listingStatus.innerHTML = searchResult.listingStatus;
                let lotSize = row.insertCell(7);
                lotSize.innerHTML = searchResult.lotSize;

            }
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetListings(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        const listings = await this.client.getAllOpenListings(this.errorHandler);
        this.dataStore.set("listings", listings);

        if (listings) {
            this.showMessage(`Here you go!`)
        } else {
            this.errorHandler("Error getting listings!  Try again...");
        }
    }

    async onSearch(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let squareFootage = document.getElementById("min-squareFootage").value;
        let price = document.getElementById("max-price").value;
        let numBedrooms = document.getElementById("min-bedroom").value;
        let numBathrooms = document.getElementById("min-bathroom").value;
        let lotSize = document.getElementById("min-lotSize").value;

        const searchedHomes = await this.client.getParameterizedListings(squareFootage, price, numBedrooms, numBathrooms, lotSize);

        this.dataStore.set("searchedHomes", searchedHomes);

        if (searchedHomes) {
            this.showMessage(`Search successful!`)
        } else {
            this.errorHandler("Error searching!  Try again...");
        }
    }

    async onCreate(event) {
        // Prevent the page from refreshing on form submit
        event.preventDefault();

        let address = document.getElementById("address").value;
        let price = document.getElementById("price").value;
        let numBedrooms = document.getElementById("bedrooms").value;
        let numBathrooms = document.getElementById("bathrooms").value;
        let squareFootage = document.getElementById("squareFootage").value;
        let listingStatus = document.getElementById("listingStatus").value;
        let lotSize = document.getElementById("lotSize").value;

        const createdListing = await this.client.createListing(address, price, numBedrooms, numBathrooms, squareFootage, listingStatus, lotSize, this.errorHandler);

        if (createdListing) {
            this.showMessage(`Created Listing!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }

    }
}

/**
 * Main method to run when the page contents have loaded.
 */
const main = async () => {
    const homePage = new HomePage();
    homePage.mount();
};

window.addEventListener('DOMContentLoaded', main);
