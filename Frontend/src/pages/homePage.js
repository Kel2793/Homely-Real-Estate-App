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
        document.getElementById('get-all-open-listings-form').addEventListener('submit', this.onSearch);
        document.getElementById('search-by-listingNumber-form').addEventListener('submit', this.onSearch);
        this.client = new ListingClient();

        this.dataStore.addChangeListener(this.renderHomeSearch);
        this.dataStore.addChangeListener(this.renderHomeListing);
        this.onGetListings();
        this.onSearch();
    }

    // Render Methods --------------------------------------------------------------------------------------------------

    async renderHomeListing() {
        let listingResultArea = document.getElementById("newListing-info");

        const newListing = this.dataStore.get("newHomes");

        if (newListing) {
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
                        <tbody>
            `;

                           // for (let newListing of newListings) {
                                listingResultArea.innerHTML += `
                                 <tr>
                                    <td> ${newListing.listingNumber}</td>
                                    <td> ${newListing.address} </td>
                                    <td> ${newListing.squareFootage} </td>
                                    <td> ${newListing.price} </td>
                                    <td> ${newListing.numBedrooms} </td>
                                    <td> ${newListing.numBathrooms} </td>
                                    <td> ${newListing.listingStatus} </td>
                                    <td> ${newListing.lotSize} </td>
                                </tr>`;
                           // }
            listingResultArea.innerHTML += `
                        </tbody>
                    </table>`;

        } else {
            listingResultArea.innerHTML = "No Item";
        }
    }


    async renderHomeSearch() {
        let resultArea = document.getElementById("result-info");
        var searchResults = [];

        searchResults = this.dataStore.get("searchedHomes");

        if (searchResults) {
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
                        <tbody>
            `;
                         for (let searchResult of searchResults) {
                            resultArea.innerHTML += `
                                 <tr>
                                    <td> ${searchResult.listingNumber}</td>
                                    <td> ${searchResult.address} </td>
                                    <td> ${searchResult.squareFootage} </td>
                                    <td> ${searchResult.price} </td>
                                    <td> ${searchResult.numBedrooms} </td>
                                    <td> ${searchResult.numBathrooms} </td>
                                    <td> ${searchResult.listingStatus} </td>
                                    <td> ${searchResult.lotSize} </td>
                                </tr>`;
                            }
            resultArea.innerHTML += `
                         </tbody>
                    </table>`;
        } else {
            resultArea.innerHTML = "No Item";
        }
    }


    // Event Handlers --------------------------------------------------------------------------------------------------

    async onGetListings() {
        let result = await this.client.getAllOpenListings(this.errorHandler);
        this.dataStore.set("listing", result);
    }

    async onSearch() {

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
        this.dataStore.set("newHomes", createdListing);

        if (createdListing) {
            this.showMessage(`Created Listing!`)
        } else {
            this.errorHandler("Error creating!  Try again...");
        }
        this.onGetListings();
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
