package njurestaurant.njutakeout.exception;

import njurestaurant.njutakeout.response.WrongResponse;

public class NotExistException extends Exception {
	private WrongResponse response;

	public NotExistException(String missingItem) {
		super(missingItem + " does not exist");
		response = new WrongResponse(10001, this.getMessage());
	}

	public WrongResponse getResponse() {
		return response;
	}

	public static void main(String []args) {
		try {
			throw new NotExistException("ID");
		} catch (NotExistException e) {
			System.out.println(e.getMessage());
		}
	}
}